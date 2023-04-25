package Controller;

import Domain.User;
import Service.Baloot;
import Service.Exceptions.InvalidCredentials;
import Service.Exceptions.UserNotFound;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    protected User login(@RequestBody Map<String, String> info) throws UserNotFound, InvalidCredentials {
        Baloot.getInstance().login(info.get("username"), info.get("password"));
        return Baloot.getInstance().findUserByUsername(info.get("username"));
    }
}