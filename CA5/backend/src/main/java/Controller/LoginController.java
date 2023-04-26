package Controller;

import Domain.User;
import Service.Baloot;
import Service.Exceptions.InvalidCredentials;
import Service.Exceptions.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    protected ResponseEntity<User> login(@RequestBody Map<String, String> info){
        try{
            Baloot.getInstance().login(info.get("username"), info.get("password"));
            return ResponseEntity.ok(Baloot.getInstance().findUserByUsername(info.get("username")));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}