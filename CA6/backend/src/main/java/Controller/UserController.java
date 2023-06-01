package Controller;

import Database.Database;
import Domain.Commodity;
import Domain.User;
import Service.Baloot;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping
public class UserController {
    @Autowired
    private Baloot baloot;

//    public UserController(Baloot baloot){
//        this.baloot = baloot;
//    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    protected ResponseEntity<User> getUser(@PathVariable String username) {
        try {
            User user = baloot.findUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @RequestMapping(value = "/users/{username}/payment", method = RequestMethod.GET)
    protected ResponseEntity<String> payment(@PathVariable String username) {
        try {
            baloot.finalizePayment(username);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

//    @RequestMapping(value = "/users/{username}/buyList", method = RequestMethod.GET)
//    protected ResponseEntity<HashMap<Integer, Integer>> getBuyList(@PathVariable String username) {
//        try {
//            return ResponseEntity.ok(baloot.findUserByUsername(username).getBuyList());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

    @RequestMapping(value = "/users/{username}/buyList/add", method = RequestMethod.POST)
    protected ResponseEntity<String> addToBuyList(@PathVariable String username, @RequestBody Map<String, String> info) {
        try {
            baloot.addToBuyList(username, Integer.valueOf(info.get("id")));
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/users/{username}/buyList/remove", method = RequestMethod.POST)
    protected ResponseEntity<String> removeFromBuyList(@PathVariable String username, @RequestBody Map<String, String> info) {
        try {
            baloot.removeFromBuyList(username, Integer.valueOf(info.get("id")));
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

//    @RequestMapping(value = "/users/{username}/purchasedList", method = RequestMethod.GET)
//    protected ResponseEntity<HashMap<Integer, Integer>> getPurchasedList(@PathVariable String username) {
//        try {
//            return ResponseEntity.ok(baloot.findUserByUsername(username).getPurchasedList());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

    @RequestMapping(value = "/users/{username}/credit", method = RequestMethod.POST)
    protected ResponseEntity<String> addCredit(@PathVariable String username, @RequestBody Map<String, String> info) {
        try {
            baloot.findUserByUsername(username).addCredit(Float.parseFloat(info.get("amount")));
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/users/{username}/discount", method = RequestMethod.POST)
    protected ResponseEntity<Float> applyDiscountCode(@PathVariable String username, @RequestBody Map<String, String> info) {
        try {
            return ResponseEntity.ok(baloot.applyDiscountCode(username, info.get("code")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @RequestMapping(value = "/users/{username}/discount", method = RequestMethod.DELETE)
    protected ResponseEntity<String> deleteDiscountCode(@PathVariable String username) {
        try {
            baloot.deleteDiscountCode(username);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

}
