package Controller;

import Exceptions.UserNotFound;
import Model.User;
import Service.AuthService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import Config.*;

import java.util.Map;

@NoArgsConstructor
@RestController
@RequestMapping
public class AuthController {
    public static final String KEY = "baloot2023";

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    protected ResponseEntity<Void> login(@RequestBody Map<String, String> user_info){
        try {
            String jwt_token = authService.login(user_info.get("username"), user_info.get("password"));
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("token", jwt_token);
            map.add("userEmail",user_info.get("username"));
            return new ResponseEntity<>(map,HttpStatus.OK);
        } catch (UserNotFound ignored) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    protected ResponseEntity<String> login(@RequestBody Map<String, String> info){
//        try{
//            authService.login(info.get("username"), info.get("password"));
//            return ResponseEntity.ok("ok");
//        } catch (Exception e){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//        }
//    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    protected ResponseEntity<Void> signup(@RequestBody Map<String, String> info){
        try{
            authService.addUser(new User(info.get("username"), info.get("password"), info.get("email"), info.get("birthDate"), info.get("address"), 0));
            authService.login(info.get("username"), info.get("password"));
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            String jwt_token = authService.login(info.get("username"), info.get("password"));
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("token", jwt_token);
            map.add("userEmail",info.get("email"));
            return new ResponseEntity<>(map,HttpStatus.OK);
//            return ResponseEntity.ok("ok");
        } catch (UserNotFound ignored) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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