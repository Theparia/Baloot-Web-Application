package Service;

import Exceptions.*;
import HTTPRequestHandler.HTTPRequestHandler;
import Model.*;
import Repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Service("userService")
public class UserService {
    private final UserRepository userRepository;
    private final BuyListItemRepository buyListItemRepository;
    private final CommodityRepository commodityRepository;
    private final PurchasedListItemRepository purchasedListItemRepository;
    private final UsedDiscountRepository usedDiscountRepository;

    public UserService(UserRepository userRepository, BuyListItemRepository buyListItemRepository,
                       CommodityRepository commodityRepository, PurchasedListItemRepository purchasedListItemRepository,
                       UsedDiscountRepository usedDiscountRepository) throws Exception {
        this.userRepository = userRepository;
        this.buyListItemRepository = buyListItemRepository;
        this.commodityRepository = commodityRepository;
        this.purchasedListItemRepository = purchasedListItemRepository;
        this.usedDiscountRepository = usedDiscountRepository;
        fetchData();
    }
    private void fetchData() throws Exception {
        final String USERS_URI = "http://5.253.25.110:5000/api/users";
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<User> users = objectMapper.readValue(HTTPRequestHandler.getRequest(USERS_URI), typeFactory.constructCollectionType(List.class, User.class));
        for(User user: users){
            Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
            if (existingUser.isPresent())
                continue;
            userRepository.save(user);
        }
    }
    public User findUserByUsername(String username) throws UserNotFound {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFound::new);
    }

    public void addToBuyList(String username, Integer commodityId) throws CommodityNotFound, UserNotFound, CommodityOutOfStock {
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

    public void removeFromBuyList(String username, Integer commodityId) throws CommodityNotFound, UserNotFound, CommodityNotInBuyList {
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
        for(BuyListItem item : buyListItem){
            map.put(item.getCommodity().getId(), item.getQuantity());
        }
        return map;
    }

    public HashMap<Integer, Integer> getPurchasedList(String username) throws UserNotFound {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        List<PurchasedListItem> purchasedList =  purchasedListItemRepository.findByUser(user);
        HashMap<Integer, Integer> map = new HashMap<>();
        for(PurchasedListItem item : purchasedList){
            map.put(item.getCommodity().getId(), item.getQuantity());
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

    public void addToPurchasedList(BuyListItem buyListItem) {
        PurchasedListItem purchasedListItem = purchasedListItemRepository.findByUserAndCommodity(buyListItem.getUser(), buyListItem.getCommodity()).orElse(null);
        if(purchasedListItem != null){
            purchasedListItem.setQuantity(purchasedListItem.getQuantity() + buyListItem.getQuantity());
        }
        else {
            purchasedListItem = new PurchasedListItem(buyListItem.getCommodity(), buyListItem.getUser(), buyListItem.getQuantity());
        }
        purchasedListItemRepository.save(purchasedListItem);
    }

    public void finalizePayment(String username) throws UserNotFound, CommodityOutOfStock, NotEnoughCredit, CommodityNotFound {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        List<BuyListItem> buylist = buyListItemRepository.findByUser(user);
        for(BuyListItem item : buylist){
            item.getCommodity().checkInStock(item.getQuantity());
        }
        user.reduceCredit(calcBuyListPrice(buylist, user));
        for(BuyListItem item : buylist){
            buyListItemRepository.delete(item);
            addToPurchasedList(item);
            item.getCommodity().reduceInStock(item.getQuantity());
            commodityRepository.save(item.getCommodity());
        }
        if(user.getCurrentDiscount() != null) {
            usedDiscountRepository.save(new UsedDiscount(user.getCurrentDiscount(), user));
            user.useDiscount();
        }
        userRepository.save(user);
    }

    public float calcBuyListPrice(List<BuyListItem> buylist, User user) {
        float totalPrice = 0;
        for(BuyListItem item : buylist){
            totalPrice += item.getQuantity() * item.getCommodity().getPrice();
        }
//        return totalPrice;
         if (user.getCurrentDiscount() == null)
             return totalPrice;
         return totalPrice * (100 - user.getCurrentDiscount().getDiscount()) / 100;
    }

}
