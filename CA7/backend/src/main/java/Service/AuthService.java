package Service;

import Exceptions.*;
import Model.User;
import Repository.UserRepository;
import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
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
        if(user.getPassword().equals(password))
            loggedInUser = user;
        else
            throw new InvalidCredentials();
    }

    public void logout() throws InvalidLogout {
        if(isUserLoggedIn())
            loggedInUser = null;
        else throw new InvalidLogout();
    }

    public void addUser(User newUser) throws InvalidUsername {
        if(!newUser.getUsername().matches("\\w+"))
            throw new InvalidUsername();
        userRepository.save(newUser);
    }
}

