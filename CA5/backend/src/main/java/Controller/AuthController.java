package Controller;

import Database.Database;
import Domain.User;
import Service.Baloot;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
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

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    protected ResponseEntity<String> signup(@RequestBody Map<String, String> info){
        try{
            Baloot.getInstance().addUser(new User(info.get("username"), info.get("password"), info.get("email"), info.get("birthDate"), info.get("address"), 0));
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