package Controller;

import Domain.User;
import Service.Baloot;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping
public class AuthController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    protected ResponseEntity<String> login(@RequestBody Map<String, String> info){
        try{
            Baloot.getInstance().login(info.get("username"), info.get("password"));
            return ResponseEntity.ok("ok");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    protected ResponseEntity<String> logout(){
        try {
            Baloot.getInstance().logout();
            return ResponseEntity.ok("ok");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    protected ResponseEntity<User> getLoggedInUser(){
        return ResponseEntity.ok(Baloot.getInstance().getLoggedInUser());
    }
}