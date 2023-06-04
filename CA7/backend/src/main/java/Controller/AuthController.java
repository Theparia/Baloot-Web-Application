package Controller;

import Model.User;
import Service.AuthService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@NoArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping
public class AuthController {
    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    protected ResponseEntity<String> login(@RequestBody Map<String, String> info){
        try{
            authService.login(info.get("username"), info.get("password"));
            return ResponseEntity.ok("ok");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    protected ResponseEntity<String> signup(@RequestBody Map<String, String> info){
        try{
            authService.addUser(new User(info.get("username"), info.get("password"), info.get("email"), info.get("birthDate"), info.get("address"), 0));
            authService.login(info.get("username"), info.get("password"));
            return ResponseEntity.ok("ok");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    protected ResponseEntity<String> logout(){
        try {
            authService.logout();
            return ResponseEntity.ok("ok");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    protected ResponseEntity<User> getLoggedInUser(){
        return ResponseEntity.ok(authService.getLoggedInUser());
    }
}