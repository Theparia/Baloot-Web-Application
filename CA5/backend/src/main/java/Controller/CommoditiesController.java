package Controller;

import Domain.Comment;
import Domain.Commodity;
import Domain.User;
import Service.Baloot;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping
public class CommoditiesController {
    @RequestMapping(value = "/commodities", method = RequestMethod.GET)
    protected List<Commodity> getCommodities() {
        return Baloot.getInstance().getCommodities();
    }

    @RequestMapping(value = "/commodities/{id}", method = RequestMethod.GET)
    protected ResponseEntity<Commodity> getCommodity(@PathVariable String id) {
        try {
            return ResponseEntity.ok(Baloot.getInstance().findCommodityById(Integer.valueOf(id)));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @RequestMapping(value = "commodities/search", method = RequestMethod.GET)
    protected ResponseEntity<List<Commodity>> searchCommodities(
            @RequestParam(value = "searchMethod", required = false) String search_method,
            @RequestParam(value = "searchedTxt", required = false) String searched_txt
    ){
        System.out.println("Search method: " + search_method);
        System.out.println("Search text: " + searched_txt);
        if(search_method.equals("name")) {
            return ResponseEntity.ok(Baloot.getInstance().searchCommoditiesByName(searched_txt));
        }
        else if(search_method.equals("category")) {
            return ResponseEntity.ok(Baloot.getInstance().searchCommoditiesByCategory(searched_txt));
        }
        else if(search_method.equals("provider")){
            return ResponseEntity.ok(Baloot.getInstance().searchCommoditiesByProviderName(searched_txt));
        }
        else{
            return ResponseEntity.ok(Baloot.getInstance().getCommodities());
        }
    }


}