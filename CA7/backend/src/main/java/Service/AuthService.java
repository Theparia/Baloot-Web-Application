package Service;

import Exceptions.*;
import Model.User;
import Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Getter
@Setter
@Service
//@Transactional
public class AuthService {
    private User loggedInUser = null;

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean isUserLoggedIn() {
        if (loggedInUser == null)
            return false;
        return true;
    }

//    public void login(String username, String password) throws UserNotFound, InvalidCredentials {
//        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
//        String hashedPassword = String.valueOf(password.hashCode());
//        if(user.getPassword().equals(hashedPassword))
//            loggedInUser = user;
//        else
//            throw new InvalidCredentials();
//    }


    public String login(String username, String password) throws UserNotFound {
        Optional<User> user = userRepository.findByUsername(username);
        String hashed_password = String.valueOf(password.hashCode());
        if (user.stream().findFirst().isEmpty())
            throw new UserNotFound();
        if (hashed_password.equals(user.get().getPassword()))
            this.loggedInUser = user.get();
        else
            throw new UserNotFound();
        return createJwtToken(username);
    }

    public void loginWithJwtToken(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        this.loggedInUser = user.get();
    }

    public String createJwtToken(String username) {
        String sign_key = "Baloot2023Baloot2023Baloot2023Baloot2023Baloot2023Baloot2023";

        SecretKey signature_type = new SecretKeySpec(sign_key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        String jwt_token = Jwts.builder()
                .claim("username", username)
                .setId(UUID.randomUUID().toString())
                .setIssuer("Baloot")                                                      // iss claim
                .setIssuedAt(Date.from(Instant.now()))                                          // iat claim
                .setExpiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS))) // exp claim
                .signWith(signature_type)
                .compact();
        return jwt_token;
    }

    public void logout() throws InvalidLogout {
        if(isUserLoggedIn())
            loggedInUser = null;
        else throw new InvalidLogout();
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

