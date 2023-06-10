package Service;

import Controller.AuthController;
import Exceptions.*;
import Model.User;
import Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@Service
public class AuthService {
    public static final String KEY = "Baloot2023Baloot2023Baloot2023Baloot2023Baloot2023Baloot2023";

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public String login(String username, String password) throws UserNotFound, InvalidCredentials {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        String hashedPassword = String.valueOf(password.hashCode());
        if (!user.getPassword().equals(hashedPassword))
            throw new InvalidCredentials();
        return createJwtToken(username);
    }

    public String createJwtToken(String username) {
        SecretKey signatureType = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        return Jwts.builder()
                .claim("username", username)
                .setId(UUID.randomUUID().toString())
                .setIssuer("Baloot")                                                      // iss claim
                .setIssuedAt(Date.from(Instant.now()))                                          // iat claim
                .setExpiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS))) // exp claim
                .signWith(signatureType)
                .compact();
    }

    public void addUser(User user) throws EmailAlreadyExists {
        try {
            String hashedPassword = String.valueOf(user.getPassword().hashCode());
            user.setPassword(hashedPassword);
            userRepository.save(user);
        } catch (Exception e){
            throw new EmailAlreadyExists();
        }
    }
}

