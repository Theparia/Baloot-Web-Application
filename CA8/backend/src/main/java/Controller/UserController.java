package Controller;


import Model.User;
import Service.DiscountService;
import Service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@RestController
@RequestMapping
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private DiscountService discountService;


    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    protected ResponseEntity<User> getUser(@PathVariable String username) {
        try {
            User user = userService.findUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @RequestMapping(value = "/users/{username}/payment", method = RequestMethod.GET)
    protected ResponseEntity<String> payment(@PathVariable String username) {
        try {
            userService.finalizePayment(username);
//            baloot.finalizePayment(username);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/users/{username}/buyList", method = RequestMethod.GET)
    protected ResponseEntity<HashMap<Integer, Integer>> getBuyList(@PathVariable String username) {
        try {
            return ResponseEntity.ok(userService.getBuyList(username));
//            return ResponseEntity.ok(baloot.findUserByUsername(username).getBuyList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @RequestMapping(value = "/users/{username}/buyList/add", method = RequestMethod.POST)
    protected ResponseEntity<String> addToBuyList(@PathVariable String username, @RequestBody Map<String, String> info) {
        try {
            userService.addToBuyList(username, Integer.valueOf(info.get("id")));
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/users/{username}/buyList/remove", method = RequestMethod.POST)
    protected ResponseEntity<String> removeFromBuyList(@PathVariable String username, @RequestBody Map<String, String> info) {
        try {
            userService.removeFromBuyList(username, Integer.valueOf(info.get("id")));
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/users/{username}/purchasedList", method = RequestMethod.GET)
    protected ResponseEntity<HashMap<Integer, Integer>> getPurchasedList(@PathVariable String username) {
        try {
            return ResponseEntity.ok(userService.getPurchasedList(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @RequestMapping(value = "/users/{username}/credit", method = RequestMethod.POST)
    protected ResponseEntity<String> addCredit(@PathVariable String username, @RequestBody Map<String, String> info) {
        try {
            userService.addCredit(username, Float.parseFloat(info.get("amount")));
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/users/{username}/discount", method = RequestMethod.POST)
    protected ResponseEntity<Float> applyDiscountCode(@PathVariable String username, @RequestBody Map<String, String> info) {
        try {
            return ResponseEntity.ok(discountService.applyDiscountCode(username, info.get("code")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @RequestMapping(value = "/users/{username}/discount", method = RequestMethod.DELETE)
    protected ResponseEntity<String> deleteDiscountCode(@PathVariable String username) {
        try {
            System.out.println("In delete discount");
            discountService.deleteDiscountCode(username);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

}
