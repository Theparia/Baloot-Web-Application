package Service;

import Domain.Commodity;
import Domain.Provider;
import Domain.Rating;
import Domain.User;
import Exceptions.*;
import HTTPRequestHandler.HTTPRequestHandler;
import Repository.CommodityRepository;
import Repository.ProviderRepository;
import Repository.RatingRepository;
import Repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Service("commodityService")
@DependsOn({"providerService", "userService"})
public class CommodityService {
    private final CommodityRepository commodityRepository;
    private final ProviderRepository providerRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    private CommodityService(CommodityRepository commodityRepository, ProviderRepository providerRepository,
                             RatingRepository ratingRepository, UserRepository userRepository) throws Exception {
        this.commodityRepository = commodityRepository;
        this.providerRepository = providerRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        fetchData();
    }

    private void fetchData() throws Exception {
        final String COMMODITIES_URI = "http://5.253.25.110:5000/api/v2/commodities";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        String rawJsonData = HTTPRequestHandler.getRequest(COMMODITIES_URI);
        List<Commodity> commodities = objectMapper.readValue(rawJsonData, typeFactory.constructCollectionType(List.class, Commodity.class));
        List<Map<String, Object>> rawDataList = objectMapper.readValue(rawJsonData, new TypeReference<>() {});
        for(int i = 0 ; i < commodities.size(); i++){
            Commodity commodity = commodities.get(i);
            Integer providerId = (Integer) rawDataList.get(i).get("providerId");
            Provider provider = providerRepository.findById(providerId).orElseThrow(ProviderNotFound::new);
            commodity.setProvider(provider);
            updateCommodityAverageRating(commodity);
            commodityRepository.save(commodity);
            //TODO: Save the first rating
//            rateCommodity("#InitialRating", commodity.getId(),commodity.getRating());
        }
    }

    public Commodity findCommodityById(Integer id) throws CommodityNotFound {
        return commodityRepository.findById(id)
                .orElseThrow(CommodityNotFound::new);
    }

    public List<Commodity> getCommodities() {
        return commodityRepository.findAll();
    }

    public List<Commodity> searchCommoditiesByCategory(String category) {
        return commodityRepository.findByCategories(category);
    }

    public List<Commodity> searchCommoditiesByName(String name) {
        return commodityRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Commodity> searchCommoditiesByProviderName(String name) {
        Provider provider = providerRepository.findByName(name);
        return commodityRepository.findByProviderId(provider.getId());
    }

    public List<Commodity> getAvailableCommodities(List<Commodity> commodities) {
        List<Commodity> availableCommodities = new ArrayList<>();
        for (Commodity commodity : commodities) {
            if (commodity.getInStock() > 0) {
                availableCommodities.add(commodity);
            }
        }
        return availableCommodities;
    }

    public List<Commodity> sortCommoditiesByPrice(List<Commodity> commodities) {
        return commodities.stream()
                .sorted(Comparator.comparing(Commodity::getPrice))
                .collect(Collectors.toList());
    }

    public List<Commodity> sortCommoditiesByName(List<Commodity> commodities) {
        return commodities.stream()
                .sorted(Comparator.comparing(Commodity::getName))
                .collect(Collectors.toList());
    }


    public List<Commodity> getCommoditiesByPage(int pageNumber, int itemsPerPage, List<Commodity> allCommodities) {
        int startIndex = pageNumber * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, allCommodities.size());
        allCommodities = allCommodities.subList(startIndex, endIndex);
        return allCommodities;
    }

    public List<Commodity> getSuggestedCommodities(Integer commodityId) throws CommodityNotFound {
        Commodity commodity = findCommodityById(commodityId);

        return getCommodities().stream()
                .filter(c -> !c.getId().equals(commodityId)) // exclude the commodity with the same ID
                .sorted(Comparator.comparing(c -> 11 * (commodity.isInSimilarCategory(c.getCategories()) ? 1 : 0) + c.getRating(), Comparator.reverseOrder()))
                .limit(4)
                .collect(Collectors.toList());
    }

    public void rateCommodity(String username, Integer commodityId, Float score) throws RatingOutOfRange, CommodityNotFound, UserNotFound {
        if (score < 1 || score > 10)
            throw new RatingOutOfRange();
        Commodity commodity = findCommodityById(commodityId);
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        Rating rating = new Rating(user, commodity, score);
        ratingRepository.save(rating);
        updateCommodityAverageRating(commodity);
        commodityRepository.save(commodity);
    }

    public void updateCommodityAverageRating(Commodity commodity){
        List<Float> scores = ratingRepository.findScoresByCommodityId(commodity.getId());
        commodity.setUserRatings(scores);
        commodity.updateAverageRating();
    }
}
