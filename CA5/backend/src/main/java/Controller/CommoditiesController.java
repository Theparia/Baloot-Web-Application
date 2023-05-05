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

    @RequestMapping(value = "/commodities/{commodityId}/comments/",method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> getCommentsCommodity(@PathVariable String commodityId) {
        System.out.println("COMMENT");
        return ResponseEntity.ok(Baloot.getInstance().getCommodityComments(Integer.valueOf(commodityId)));
    }

    @RequestMapping(value = "/commodities/{id}", method = RequestMethod.GET)
    protected ResponseEntity<Commodity> getCommodity(@PathVariable String id) {
        try {
            return ResponseEntity.ok(Baloot.getInstance().findCommodityById(Integer.valueOf(id)));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @RequestMapping(value = "/commodities/size/",method = RequestMethod.GET)
    public ResponseEntity<Integer> sortCommoditiesSize(
            @RequestParam("sortMethod") String sortMethod,
            @RequestParam(value = "searchMethod", required = false) String searchMethod,
            @RequestParam(value = "searchedText", required = false) String searchedText,
            @RequestParam(value = "commoditiesAvailable", required = false) Boolean commoditiesAvailable) {
        Integer size = filterCommodities(sortMethod, searchMethod, searchedText, commoditiesAvailable).size();
        return ResponseEntity.ok(size);
    }

    @RequestMapping(value = "/commodities/",method = RequestMethod.GET)
    public ResponseEntity<List<Commodity>> getCommodities(
            @RequestParam("sortMethod") String sortMethod,
            @RequestParam(value = "searchMethod", required = false) String searchMethod,
            @RequestParam(value = "searchedText", required = false) String searchedText,
            @RequestParam(value = "commoditiesAvailable", required = false) Boolean commoditiesAvailable,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize) {

        List<Commodity> commodities = filterCommodities(sortMethod, searchMethod, searchedText, commoditiesAvailable);
        return ResponseEntity.ok(Baloot.getInstance().getCommoditiesByPage(pageNumber, pageSize, commodities));
    }

    private List<Commodity> filterCommodities(String sortMethod, String searchMethod, String searchedText, Boolean commoditiesAvailable){
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
        return commodities;
    }
}