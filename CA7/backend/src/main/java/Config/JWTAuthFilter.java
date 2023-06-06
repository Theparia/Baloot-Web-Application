package Config;

import Controller.AuthController;
import Service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.annotation.WebFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

@Component
@Order(2)
@WebFilter(filterName = "JWTAuthFilter", asyncSupported = true, urlPatterns = {"/*"})
public class JWTAuthFilter implements Filter {

    @Autowired
    AuthService authService;
    private static final ArrayList<String> excludedUrls = new ArrayList<>() {
        {
            add("login");
            add("signup");
        }
    };

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String[] path = request.getRequestURI().split("/");

        if (excludedUrls.contains(path[1])) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || authHeader.split(" ").length < 2) { //TODO: < 2?
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"No JWT token\"}");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return;
        }

        String jwt = authHeader.split(" ")[1];
        SecretKey key = new SecretKeySpec(AuthController.KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
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
