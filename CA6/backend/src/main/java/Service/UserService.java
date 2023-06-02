package Service;

import Domain.BuyListItem;
import Domain.Commodity;
import Domain.User;
import Exceptions.*;
import HTTPRequestHandler.HTTPRequestHandler;
import Repository.BuyListItemRepository;
import Repository.CommodityRepository;
import Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Service("userService")
public class UserService {
    private final UserRepository userRepository;
    private final BuyListItemRepository buyListItemRepository;
    private final CommodityRepository commodityRepository;

    public UserService(UserRepository userRepository, BuyListItemRepository buyListItemRepository, CommodityRepository commodityRepository) throws Exception {
        this.userRepository = userRepository;
        this.buyListItemRepository = buyListItemRepository;
        this.commodityRepository = commodityRepository;
        fetchData();
    }
    private void fetchData() throws Exception {
        final String USERS_URI = "http://5.253.25.110:5000/api/users";
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<User> users = objectMapper.readValue(HTTPRequestHandler.getRequest(USERS_URI), typeFactory.constructCollectionType(List.class, User.class));
        userRepository.saveAll(users);
    }
    public User findUserByUsername(String username) throws UserNotFound {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFound::new);
    }

    public void addToBuyList(String username, Integer commodityId) throws CommodityNotFound, UserNotFound, CommodityOutOfStock, CommodityAlreadyExistsInBuyList {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        Commodity commodity = commodityRepository.findById(commodityId).orElseThrow(CommodityNotFound::new);
        int quantity = 1;
        BuyListItem buyListItem = buyListItemRepository.findByUserAndCommodity(user, commodity).orElse(null);
        if(buyListItem != null){
            quantity += buyListItem.getQuantity();
            commodity.checkInStock(quantity);
            buyListItem.setQuantity(quantity);
        }
        else {
            buyListItem = new BuyListItem(commodity, user, quantity);
        }
        buyListItemRepository.save(buyListItem);
    }

    public void removeFromBuyList(String username, Integer commodityId) throws CommodityNotFound, UserNotFound, CommodityOutOfStock, CommodityAlreadyExistsInBuyList, CommodityNotInBuyList {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        Commodity commodity = commodityRepository.findById(commodityId).orElseThrow(CommodityNotFound::new);
        BuyListItem buyListItem = buyListItemRepository.findByUserAndCommodity(user, commodity).orElseThrow(CommodityNotInBuyList::new);
        if(buyListItem.getQuantity() == 1){
            buyListItemRepository.delete(buyListItem);
        }
        else{
            buyListItem.setQuantity(buyListItem.getQuantity()-1);
            buyListItemRepository.save(buyListItem);
        }
    }

    public HashMap<Integer, Integer> getBuyList(String username) throws UserNotFound {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        List<BuyListItem> buyListItem =  buyListItemRepository.findByUser(user);
        HashMap<Integer, Integer> map = new HashMap<>();
        for(BuyListItem b : buyListItem){
            map.put(b.getCommodity().getId(), b.getQuantity());
        }
        return map;
    }

    public void addCredit(String username, float creditToBeAdded) throws NegativeCredit, UserNotFound {
        if(creditToBeAdded <= 0)
            throw new NegativeCredit();
        User user = findUserByUsername(username);
        user.setCredit(creditToBeAdded + user.getCredit());
        userRepository.save(user);
    }

}
