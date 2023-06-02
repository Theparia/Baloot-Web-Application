package Service;

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
@Service("providerService")
public class ProviderService {
    private final ProviderRepository providerRepository;
    private final CommodityRepository commodityRepository;

    private ProviderService(ProviderRepository providerRepository, CommodityRepository commodityRepository){
        this.providerRepository = providerRepository;
        this.commodityRepository = commodityRepository;
        try {
            fetchData();
        } catch (Exception ignored){

        }
    }

    private void fetchData() throws Exception {
        final String PROVIDERS_URI = "http://5.253.25.110:5000/api/v2/providers";
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<Provider> providers = objectMapper.readValue(HTTPRequestHandler.getRequest(PROVIDERS_URI), typeFactory.constructCollectionType(List.class, Provider.class));
        providerRepository.saveAll(providers);
    }

    public Provider findProviderById(Integer id) throws CommodityNotFound {
        return providerRepository.findById(id)
                .orElseThrow(CommodityNotFound::new);
    }

    public List<Commodity> findCommoditiesByProvider(Integer providerId) throws CommodityNotFound {
        return commodityRepository.findCommoditiesByProvider(findProviderById(providerId));
    }

}
