package Service;


import Domain.Discount;
import Domain.Provider;
import Domain.UsedDiscount;
import Domain.User;
import Exceptions.CommodityNotFound;
import Exceptions.ExpiredDiscount;
import Exceptions.InvalidDiscount;
import Exceptions.UserNotFound;
import HTTPRequestHandler.HTTPRequestHandler;
import Repository.DiscountRepository;
import Repository.ProviderRepository;
import Repository.UsedDiscountRepository;
import Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Service
@DependsOn("userService")
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final UserRepository userRepository;
    private final UsedDiscountRepository usedDiscountRepository;

    private DiscountService(DiscountRepository discountRepository, UserRepository userRepository, UsedDiscountRepository usedDiscountRepository) throws Exception {
        this.discountRepository = discountRepository;
        this.userRepository = userRepository;
        this.usedDiscountRepository = usedDiscountRepository;
        fetchData();
    }

    private void fetchData() throws Exception {
        final String DISCOUNT_URI = "http://5.253.25.110:5000/api/discount";
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<Discount> discounts = objectMapper.readValue(HTTPRequestHandler.getRequest(DISCOUNT_URI), typeFactory.constructCollectionType(List.class, Discount.class));
        discountRepository.saveAll(discounts);
    }

    public float applyDiscountCode(String username, String discountCode) throws UserNotFound, ExpiredDiscount, InvalidDiscount {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        Discount discount = discountRepository.findByDiscountCode(discountCode).orElseThrow(InvalidDiscount::new);
        UsedDiscount usedDiscount = usedDiscountRepository.findByDiscountAndUser(discount, user).orElse(null);
        if(usedDiscount != null){
            throw new ExpiredDiscount();
        }
        user.setCurrentDiscount(discount);
        userRepository.save(user);
        System.out.println("&&&" + user.getCurrentDiscount().getDiscountCode());
        return discount.getDiscount();
    }

    public void deleteDiscountCode(String username) throws UserNotFound {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        user.deleteCurrentDiscount();
        userRepository.save(user);
    }
}
