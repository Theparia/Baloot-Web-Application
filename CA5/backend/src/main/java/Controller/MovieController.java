package Controller;


import Domain.User;
import Service.Baloot;
import Service.Exceptions.InvalidCredentials;
import Service.Exceptions.UserNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class MovieController {
    @RequestMapping(value = "/", method = RequestMethod.POST)
    protected ResponseEntity<User> login(@RequestBody Map<String, String> info) throws UserNotFound, InvalidCredentials {
        Baloot.getInstance().login(info.get("username"), info.get("password"));
        return ResponseEntity.ok(Baloot.getInstance().findUserByUsername(info.get("username")));
    }
}
