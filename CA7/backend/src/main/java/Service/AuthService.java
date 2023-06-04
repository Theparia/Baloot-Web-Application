package Service;

import Exceptions.*;
import Model.User;
import Repository.UserRepository;
import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Getter
@Setter
@Service
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

    public void login(String username, String password) throws UserNotFound, InvalidCredentials {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        String hashedPassword = String.valueOf(password.hashCode());
        if(user.getPassword().equals(hashedPassword))
            loggedInUser = user;
        else
            throw new InvalidCredentials();
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

