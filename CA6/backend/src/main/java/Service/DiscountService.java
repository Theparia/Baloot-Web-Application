package Service;


import Domain.Discount;
import Domain.Provider;
import HTTPRequestHandler.HTTPRequestHandler;
import Repository.DiscountRepository;
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
public class DiscountService {
    private final DiscountRepository discountRepository;

    private DiscountService(DiscountRepository discountRepository){
        this.discountRepository = discountRepository;
        try {
            fetchData();
        } catch (Exception ignored){

        }
    }

    private void fetchData() throws Exception {
        final String DISCOUNT_URI = "http://5.253.25.110:5000/api/discount";
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<Discount> discounts = objectMapper.readValue(HTTPRequestHandler.getRequest(DISCOUNT_URI), typeFactory.constructCollectionType(List.class, Discount.class));
        discountRepository.saveAll(discounts);
    }

}
