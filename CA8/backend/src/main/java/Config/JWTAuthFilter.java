package Config;

import Service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

@Component
@Order(2)
public class JWTAuthFilter extends OncePerRequestFilter {
    private static final ArrayList<String> excludedUrls = new ArrayList<>() {
        {
            add("login");
            add("signup");
            add("callback");
        }
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        String[] path = request.getRequestURI().split("/");

        if (excludedUrls.contains(path[1])) {
            filterChain.doFilter(request, response);
            return;
        }

        if (jwt == null || jwt.equals("")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"No JWT token\"}");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return;
        }

        SecretKey key = new SecretKeySpec(AuthService.KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Jws<Claims> jwsClaims;
        try {
            jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt);
            if (jwsClaims.getBody().getExpiration().before(new Date()))
                throw new JwtException("Token is expired");
            request.setAttribute("username", jwsClaims.getBody().get("username")); //TODO: user or username

        } catch (JwtException e) {
            System.out.println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"BAD JWT\"}");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return;
        }
        filterChain.doFilter(request, response);
    }
}