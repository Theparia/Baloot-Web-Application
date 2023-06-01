package Service;

import Database.Database;
import Domain.Commodity;
import Domain.Provider;
import Exceptions.CommodityNotFound;
import HTTPRequestHandler.HTTPRequestHandler;
import Repository.CommodityRepository;
import Repository.ProviderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Service
public class CommodityService {
    private final CommodityRepository commodityRepository;

    private CommodityService(CommodityRepository commodityRepository) throws Exception {
        this.commodityRepository = commodityRepository;
//        try {
            fetchData();
//        } catch (Exception ignored){
//
//        }
    }

    private void fetchData() throws Exception {
        final String COMMODITIES_URI = "http://5.253.25.110:5000/api/v2/commodities";
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<Commodity> commodities = objectMapper.readValue(HTTPRequestHandler.getRequest(COMMODITIES_URI), typeFactory.constructCollectionType(List.class, Commodity.class));
        commodityRepository.saveAll(commodities);
    }

    public Commodity findCommodityById(Integer id) throws CommodityNotFound {
        return commodityRepository.findById(id)
                .orElseThrow(CommodityNotFound::new);
    }
}
