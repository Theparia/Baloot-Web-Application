package Controller;

import Database.Database;
import Domain.Commodity;
import Domain.User;
import Service.Baloot;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

//    @RequestMapping(value = "/users/{username}/buyList", method = RequestMethod.GET)
//    protected ResponseEntity<HashMap<Commodity, Integer>> getBuyList(@PathVariable String username){
//        try {
//            Baloot.getInstance();
//            HashMap<Commodity, Integer> temp = new HashMap<>();
//            temp.put(Database.getInstance().getCommodities().get(0), 5);
//            temp.put(Database.getInstance().getCommodities().get(1), 2);
//            return ResponseEntity.ok(temp);
////            return ResponseEntity.ok(Baloot.getInstance().findUserByUsername(username).getBuyList());
//        } catch (Exception e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }


    @RequestMapping(value = "/users/{username}/buyList", method = RequestMethod.GET)
    protected ResponseEntity<List<Map<String, Object>>> getBuyList(@PathVariable String username){ //TODO
        try {
            Baloot.getInstance();
            HashMap<Commodity, Integer> buyList = new HashMap<>();
            buyList.put(Database.getInstance().getCommodities().get(0), 1);
            buyList.put(Database.getInstance().getCommodities().get(1), 2);
//            User user = Baloot.getInstance().findUserByUsername(username);
//            Map<Commodity, Integer> buyList = user.getBuyList();

            List<Map<String, Object>> response = new ArrayList<>();
            for (Map.Entry<Commodity, Integer> entry : buyList.entrySet()) {
                Commodity commodity = entry.getKey();
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> commodityMap = mapper.convertValue(commodity, new TypeReference<Map<String, Object>>() {});
                commodityMap.put("quantity", entry.getValue());
                response.add(commodityMap);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @RequestMapping(value = "/users/{username}/purchasedList", method = RequestMethod.GET)
    protected ResponseEntity<List<Map<String, Object>>> getPurchasedList(@PathVariable String username){ //TODO
        try {
            Baloot.getInstance();
            HashMap<Commodity, Integer> buyList = new HashMap<>();
            buyList.put(Database.getInstance().getCommodities().get(2), 3);
            buyList.put(Database.getInstance().getCommodities().get(3), 4);
//            User user = Baloot.getInstance().findUserByUsername(username);
//            Map<Commodity, Integer> buyList = user.getBuyList();

            List<Map<String, Object>> response = new ArrayList<>();
            for (Map.Entry<Commodity, Integer> entry : buyList.entrySet()) {
                Commodity commodity = entry.getKey();
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> commodityMap = mapper.convertValue(commodity, new TypeReference<Map<String, Object>>() {});
                commodityMap.put("quantity", entry.getValue());
                response.add(commodityMap);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

//    @RequestMapping(value = "/users/{username}/purchasedList", method = RequestMethod.GET)
//    protected ResponseEntity<HashMap<Commodity, Integer>> getPurchasedList(@PathVariable String username){
//        try {
//            return ResponseEntity.ok(Baloot.getInstance().findUserByUsername(username).getPurchasedList());
//        } catch (Exception e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

    @RequestMapping(value = "/users/{username}/credit", method = RequestMethod.POST)
    protected ResponseEntity<String> addCredit(@PathVariable String username, @RequestBody Map<String, String> info){
        try {
            Baloot.getInstance().findUserByUsername(username).addCredit(Float.parseFloat(info.get("amount")));
            return ResponseEntity.ok("ok");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

}
