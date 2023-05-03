package Controller;

import Domain.Comment;
import Domain.Commodity;
import Domain.User;
import Service.Baloot;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
            @RequestParam(value = "searchedText", required = false) String searchedText
    ){
        System.out.println("Search method: " + search_method);
        System.out.println("Search text: " + searchedText);
        if(search_method.equals("name")) {
            return ResponseEntity.ok(Baloot.getInstance().searchCommoditiesByName(searchedText));
        }
        else if(search_method.equals("category")) {
            return ResponseEntity.ok(Baloot.getInstance().searchCommoditiesByCategory(searchedText));
        }
        else if(search_method.equals("provider")){
            return ResponseEntity.ok(Baloot.getInstance().searchCommoditiesByProviderName(searchedText));
        }
        else{
            return ResponseEntity.ok(Baloot.getInstance().getCommodities());
        }
    }

    @RequestMapping(value = "commodities/available", method = RequestMethod.GET)
    protected ResponseEntity<List<Commodity>> searchCommodities() {
        return ResponseEntity.ok(Baloot.getInstance().getAvailableCommodities(Baloot.getInstance().getCommodities()));
    }

    @RequestMapping(value = "/commodities/filter",method = RequestMethod.GET)
    public ResponseEntity<List<Commodity>> sortCommodities(
            @RequestParam("sortMethod") String sortMethod,
            @RequestParam(value = "searchMethod", required = false) String searchMethod,
            @RequestParam(value = "searchedText", required = false) String searchedText,
            @RequestParam(value = "commoditiesAvailable", required = false) Boolean commoditiesAvailable) {

        List<Commodity> commodities = Baloot.getInstance().getCommodities();

        if (searchMethod != null && !searchedText.equals("")) {
            if (searchMethod.equals("category")) {
                commodities = Baloot.getInstance().searchCommoditiesByCategory(searchedText);
            } else if (searchMethod.equals("name")) {
                commodities = Baloot.getInstance().searchCommoditiesByName(searchedText);
            } else if (searchMethod.equals("provider")) {
                commodities = Baloot.getInstance().searchCommoditiesByProviderName(searchedText);
            }
        }
        if (commoditiesAvailable) {
            commodities = Baloot.getInstance().getAvailableCommodities(commodities);
        }
        if (sortMethod.equals("name")) {
            commodities = Baloot.getInstance().sortCommoditiesByName(commodities);
        }
        if (sortMethod.equals("price")) {
            commodities = Baloot.getInstance().sortCommoditiesByPrice(commodities);
        }
        return ResponseEntity.ok(commodities);
    }
}