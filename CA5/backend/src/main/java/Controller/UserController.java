package Controller;

import Database.Database;
import Domain.Commodity;
import Domain.User;
import Service.Baloot;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping
public class UserController {
    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    protected ResponseEntity<User> getUser(@PathVariable String username) {
        try {
            User user = Baloot.getInstance().findUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @RequestMapping(value = "/users/{username}/buyList", method = RequestMethod.GET)
    protected ResponseEntity<HashMap<Commodity, Integer>> getBuyList(@PathVariable String username){
        try {
            return ResponseEntity.ok(Baloot.getInstance().findUserByUsername(username).getBuyList());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @RequestMapping(value = "/users/{username}/purchasedList", method = RequestMethod.GET)
    protected ResponseEntity<HashMap<Commodity, Integer>> getPurchasedList(@PathVariable String username){
        try {
            return ResponseEntity.ok(Baloot.getInstance().findUserByUsername(username).getPurchasedList());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
