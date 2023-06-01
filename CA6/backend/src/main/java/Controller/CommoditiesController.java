package Controller;

import Domain.Comment;
import Domain.Commodity;
import Service.Baloot;
import Exceptions.CommodityNotFound;
import Service.CommentService;
import Service.CommodityService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping
public class CommoditiesController {
    @Autowired
    private Baloot baloot;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/commodities/{id}", method = RequestMethod.GET)
    protected ResponseEntity<Commodity> getCommodity(@PathVariable String id) {
        try {
            return ResponseEntity.ok(commodityService.findCommodityById(Integer.valueOf(id)));
//            return ResponseEntity.ok(baloot.findCommodityById(Integer.valueOf(id)));
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
        return ResponseEntity.ok(baloot.getCommoditiesByPage(pageNumber, pageSize, commodities));
    }

    @RequestMapping(value = "/commodities/{commodityId}/suggested/",method = RequestMethod.GET)
    public ResponseEntity<List<Commodity>> getSuggestedCommodities(@PathVariable String commodityId) throws CommodityNotFound {
        return ResponseEntity.ok(baloot.getSuggestedCommodities(Integer.valueOf(commodityId)));
    }

    @RequestMapping(value = "/commodities/{commodityId}/comments/",method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> getCommentsCommodity(@PathVariable String commodityId) {
        return ResponseEntity.ok(commentService.getCommodityComments(Integer.valueOf(commodityId)));
//        return ResponseEntity.ok(baloot.getCommodityComments(Integer.valueOf(commodityId)));
    }

//    @RequestMapping(value = "/commodities/{commodityId}/comments/like",method = RequestMethod.POST)
//    public ResponseEntity<List<Comment>> likeComment(@PathVariable String commodityId, @RequestParam("commentId") String sortMethod) {
//        return ResponseEntity.ok(baloot.getCommodityComments(Integer.valueOf(commodityId)));
//    }

    @RequestMapping(value = "/commodities/{id}/rating", method = RequestMethod.POST)
    protected ResponseEntity<String> rateCommodity(@PathVariable String id, @RequestBody Map<String, String> info) {
        try {
            baloot.rateCommodity(info.get("username"), Integer.valueOf(id), Integer.valueOf(info.get("score")));
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    private List<Commodity> filterCommodities(String sortMethod, String searchMethod, String searchedText, Boolean commoditiesAvailable){
        List<Commodity> commodities = baloot.getCommodities();

        if (searchMethod != null && !searchedText.equals("")) {
            if (searchMethod.equals("category")) {
                commodities = baloot.searchCommoditiesByCategory(searchedText);
            } else if (searchMethod.equals("name")) {
                commodities = baloot.searchCommoditiesByName(searchedText);
            } else if (searchMethod.equals("provider")) {
                commodities = baloot.searchCommoditiesByProviderName(searchedText);
            }
        }
        if (commoditiesAvailable) {
            commodities = baloot.getAvailableCommodities(commodities);
        }
        if (sortMethod.equals("name")) {
            commodities = baloot.sortCommoditiesByName(commodities);
        }
        if (sortMethod.equals("price")) {
            commodities = baloot.sortCommoditiesByPrice(commodities);
        }
        return commodities;
    }
}