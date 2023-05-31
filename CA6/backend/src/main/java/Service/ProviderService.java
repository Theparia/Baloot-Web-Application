package Service;

import Domain.Provider;
import HTTPRequestHandler.HTTPRequestHandler;
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
public class ProviderService {
    private final ProviderRepository providerRepository;

    private ProviderService(ProviderRepository providerRepository){
        this.providerRepository = providerRepository;
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

}
