//package Controller;
//
//import Domain.Commodity;
//import Service.Baloot;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping
//public class CommoditiesController {
//    @RequestMapping(value = "/commodities", method = RequestMethod.GET)
//    protected List<Commodity> getCommodities() {
//        return Baloot.getInstance().getCommodities();
//    }
//
////    @RequestMapping(value = "/commodities/sort",method = RequestMethod.GET)
////    public List<Commodity> searchMovies(@RequestParam("select_option") String select_option, @RequestParam(value = "sort_option", required = false) String search_method,
////            @RequestParam(value = "searchedTxt", required = false) String searched_txt){
////        List<Commodity> commodities = new ArrayList<>();
////    }
//
//}