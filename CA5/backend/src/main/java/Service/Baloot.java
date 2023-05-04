package Service;

import Database.Database;
import Domain.*;
import Service.Exceptions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import HTTPRequestHandler.HTTPRequestHandler;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Baloot {

    private static Baloot instance = null;
    private User loggedInUser = null;


    private Baloot(){
        try {
            importDatabase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Baloot getInstance() {
        if (instance == null) {
            instance = new Baloot();
        }
        return instance;
    }
    public List<Commodity> getCommodities(){
        return Database.getInstance().getCommodities();
    }

    public List<Commodity> getCommoditiesByPage(int pageNumber, int itemsPerPage, List<Commodity> allCommodities) {
        int startIndex = pageNumber * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, allCommodities.size());
        System.out.println("pageNumber:"+pageNumber + " itemsPerPage:" +itemsPerPage + " start:" + startIndex + " end:" + endIndex + " allCommodities.size():" + allCommodities.size());
        allCommodities = allCommodities.subList(startIndex, endIndex);
        int i = 0;
        for(Commodity commodity: allCommodities){
            System.out.println("i=" + i + " " + commodity.getName());
            i+=1;
        }
        return allCommodities;
    }

    public void importDatabase() throws Exception {
        final String USERS_URI = "http://5.253.25.110:5000/api/users";
        final String COMMODITIES_URI = "http://5.253.25.110:5000/api/v2/commodities";
        final String PROVIDERS_URI = "http://5.253.25.110:5000/api/providers";
        final String COMMENTS_URI = "http://5.253.25.110:5000/api/comments";
        final String DISCOUNT_URI = "http://5.253.25.110:5000/api/discount";

        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();

        List<User> users = objectMapper.readValue(HTTPRequestHandler.getRequest(USERS_URI), typeFactory.constructCollectionType(List.class, User.class));
        Database.getInstance().setUsers(users);

        List<Commodity> commodities = objectMapper.readValue(HTTPRequestHandler.getRequest(COMMODITIES_URI), typeFactory.constructCollectionType(List.class, Commodity.class));
        Database.getInstance().setCommodities(commodities);

        List<Provider> providers = objectMapper.readValue(HTTPRequestHandler.getRequest(PROVIDERS_URI), typeFactory.constructCollectionType(List.class, Provider.class));
        Database.getInstance().setProviders(providers);

        List<Comment> comments = objectMapper.readValue(HTTPRequestHandler.getRequest(COMMENTS_URI), typeFactory.constructCollectionType(List.class, Comment.class));
        Database.getInstance().setComments(comments);

        List<Discount> discounts = objectMapper.readValue(HTTPRequestHandler.getRequest(DISCOUNT_URI), typeFactory.constructCollectionType(List.class, Discount.class));
        Database.getInstance().setDiscounts(discounts);
    }

    public boolean isUserLoggedIn() {
        if (loggedInUser == null)
            return false;
        return true;
    }

    public void login(String username, String password) throws UserNotFound, InvalidCredentials {
        User user = findUserByUsername(username);
        if(user.getPassword().equals(password))
            loggedInUser = user;
        else
            throw new InvalidCredentials();
    }

    public void logout() throws InvalidLogout {
        if(isUserLoggedIn())
            loggedInUser = null;
        else throw new InvalidLogout();
    }

    public User findUserByUsername(String username) throws UserNotFound {
        for (User user : Database.getInstance().getUsers()){
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        throw new UserNotFound();
    }

    public void addUser(User newUser) throws InvalidUsername {
        try{
            findUserByUsername(newUser.getUsername()).update(newUser);
        } catch (UserNotFound userNotFoundError){
             if(!newUser.getUsername().matches("\\w+"))
                 throw new InvalidUsername();
             Database.getInstance().addUser(newUser);
        }
    }

     public void addProvider(Provider newProvider) {
         Database.getInstance().addProvider(newProvider);
     }

     public Provider findProviderById(Integer providerId) {
        for (Provider provider : Database.getInstance().getProviders()){
            if(provider.isEqual(providerId)){
                return provider;
            }
        }
        return null;
     }

    public Commodity findCommodityById(Integer commodityId) throws CommodityNotFound {
        for (Commodity commodity : Database.getInstance().getCommodities()){
            if(commodity.isEqual(commodityId)){
                return commodity;
            }
        }
        throw new CommodityNotFound();
    }

    public List<Commodity> searchCommoditiesByName(String name) {
        return Database.getInstance().getCommodities().stream()
                .filter(commodity -> commodity.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Commodity> searchCommoditiesByCategory(String category) {
        return Database.getInstance().getCommodities().stream()
                .filter(commodity -> commodity.getCategories().contains(category))
                .collect(Collectors.toList());
    }

    public List<Commodity> searchCommoditiesByProviderName(String name) {
        return Database.getInstance().getCommodities().stream()
                .filter(commodity -> findProviderById(commodity.getProviderId()).getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Commodity> sortCommoditiesByPrice(List<Commodity> commodities){
        return commodities.stream()
                .sorted(Comparator.comparing(Commodity::getPrice))
                .collect(Collectors.toList());
    }

    public List<Commodity> sortCommoditiesByName(List<Commodity> commodities){
        return commodities.stream()
                .sorted(Comparator.comparing(Commodity::getName))
                .collect(Collectors.toList());
    }

    public List<Commodity> findCommoditiesByCategory(String category){
        List<Commodity> commoditiesInCategory = new ArrayList<>();
        for (Commodity commodity : Database.getInstance().getCommodities()){
            if(commodity.isInCategory(category)){
                commoditiesInCategory.add(commodity);
            }
        }
        return commoditiesInCategory;
    }

    public Discount findDiscountByCode(String code) throws InvalidDiscount {
        for(Discount discount : Database.getInstance().getDiscounts()){
            if(discount.getDiscountCode().equals(code)){
                return discount;
            }
        }
        throw new InvalidDiscount();
    }

    public float applyDiscountCode(String username, String discountCode) throws UserNotFound, ExpiredDiscount, InvalidDiscount, CommodityNotFound {
        User user = findUserByUsername(username);
        Discount discount = findDiscountByCode(discountCode);
        user.setCurrentDiscount(discount);
        return discount.getDiscount();
    }

    public void deleteDiscountCode(String username) throws UserNotFound, ExpiredDiscount, InvalidDiscount, CommodityNotFound {
        User user = findUserByUsername(username);
        user.deleteCurrentDiscount();
    }

    public void addComment(String userEmail, Integer commodityId, String text){
        Comment comment = new Comment(commodityId, userEmail, text, LocalDate.now().toString());
        Database.getInstance().addComment(comment);
    }

     public void addCommodity(Commodity newCommodity) throws ProviderNotFound {
        findProviderById(newCommodity.getProviderId());
        Database.getInstance().addCommodity(newCommodity);
     }

     public void addToBuyList(String username, Integer commodityId) throws CommodityNotFound, UserNotFound, CommodityOutOfStock, CommodityAlreadyExistsInBuyList {
        findCommodityById(commodityId).checkInStock(1);
        findUserByUsername(username).addToBuyList(commodityId);
     }

     public float calcBuyListPrice(String username) throws UserNotFound, CommodityNotFound { //TODO: where?
         User user = findUserByUsername(username);
         float totalPrice = 0;

         for (var entry : user.getBuyList().entrySet()) {
             totalPrice += entry.getValue() * findCommodityById(entry.getKey()).getPrice();
         }

         if (user.getCurrentDiscount() == null)
             return totalPrice;
         return totalPrice * (100 - user.getCurrentDiscount().getDiscount()) / 100;
     }

    public void finalizePayment(String username) throws UserNotFound, CommodityOutOfStock, NotEnoughCredit, CommodityNotFound {
        User user = findUserByUsername(username);
//        Set<Integer> buyListCommodityIds = user.getBuyList().keySet();

        for (var entry : user.getBuyList().entrySet()) {
            Integer commodityId = entry.getKey();
            Integer quantity = entry.getValue();
            findCommodityById(commodityId).checkInStock(quantity);
        }

        user.reduceCredit(calcBuyListPrice(username));
        for (var entry : user.getBuyList().entrySet()) {
            Integer commodityId = entry.getKey();
            Integer quantity = entry.getValue();
            user.addToPurchasedList(commodityId);
            findCommodityById(commodityId).reduceInStock(quantity);
        }
        user.useDiscount();
        user.resetBuyList();
    }

     public void removeFromBuyList(String username, Integer commodityId) throws UserNotFound, CommodityNotInBuyList {
            findUserByUsername(username).removeFromBuyList(commodityId);
     }

     public List<Comment> getCommodityComments(Integer commodityId){
        List<Comment> comments = new ArrayList<>();
        for (Comment comment : Database.getInstance().getComments()){
            if (comment.getCommodityId().equals(commodityId)){
                comments.add(comment);
            }
        }
        return comments;
     }

     public String getUsernameByEmail(String email) throws UserNotFound {
        for (User user : Database.getInstance().getUsers()){
            if (user.getEmail().equals(email)){
                return user.getUsername();
            }
        }
        throw new UserNotFound();
     }

    public Comment findCommentById(Integer commentId) throws CommentNotFound {
        for (Comment comment : Database.getInstance().getComments()){
            if(comment.getId().equals(commentId)){
                return comment;
            }
        }
        throw new CommentNotFound();
    }

    public void rateCommodity(String username, Integer commodityId, Integer score) throws UserNotFound, CommodityNotFound, RatingOutOfRange {
        findCommodityById(commodityId).addUserRating(findUserByUsername(username).getUsername(), score);
    }

    public List<Commodity> findCommoditiesByProvider(Integer proveiderId){
        List<Commodity> commodities = new ArrayList<>();
        for(Commodity commodity : Database.getInstance().getCommodities()){
            if(commodity.getProviderId().equals(proveiderId))
                commodities.add(commodity);
        }
        return commodities;
    }

    private boolean isPriceIntervalValid(float startPrice, float endPrice){
        return startPrice >= 0 && endPrice >=0 && startPrice <= endPrice;
    }

    public List<Commodity> searchCommoditiesByPrice(float startPrice, float endPrice) throws InvalidPriceInterval {
        if(!isPriceIntervalValid(startPrice, endPrice))
            throw new InvalidPriceInterval();
        List<Commodity> commodities = new ArrayList<>();
        for(Commodity commodity : Database.getInstance().getCommodities()){
            if(commodity.getPrice() >= startPrice && commodity.getPrice() <= endPrice)
                commodities.add(commodity);
        }
        return commodities;
    }

    public void addUserCredit(String username, float credit) throws UserNotFound, NegativeCredit {
        findUserByUsername(username).addCredit(credit);
    }

    private boolean isVoteValid(int vote){
        return vote == 1 || vote == 0 || vote == -1;
    }

    public void voteComment(Integer commentId, String username, int vote) throws InvalidCommentVote, CommentNotFound, UserNotFound{
        if(!isVoteValid(vote))
            throw new InvalidCommentVote();
        findCommentById(commentId).addVote(findUserByUsername(username).getUsername(), vote);
    }

    public double getAverageRating(String providerId){
        List<Commodity> providedCommodities = findCommoditiesByProvider(Integer.valueOf(providerId));
        double averageRating = providedCommodities.stream()
                .mapToDouble(commodity -> commodity.getRating())
                .average()
                .orElse(Double.NaN);
        return averageRating;
    }

    public List<Commodity> getSuggestedCommodities(Integer commodityId) throws CommodityNotFound {
        Commodity commodity = findCommodityById(commodityId);

        return Database.getInstance().getCommodities().stream()
                .sorted(Comparator.comparing(c -> 11 * (commodity.isInSimilarCategory(c.getCategories()) ? 1 : 0) + c.getRating(), Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toList());

    }

    public List<Commodity> getAvailableCommodities(List<Commodity> commodities_){
        List<Commodity> availableCommodities = new ArrayList<>();
        for (Commodity commodity : commodities_){
            if(commodity.getInStock() > 0){
                availableCommodities.add(commodity);
            }
        }
        return availableCommodities;
    }

    public void printData(){
        System.out.println("Users:");
        for(User user : Database.getInstance().getUsers()){
            System.out.println(user.getUsername());
        }
        System.out.println("Providers:");
        for(Provider provider : Database.getInstance().getProviders()){
            System.out.println(provider.getId());
        }
        System.out.println("Commodities:");
        for(Commodity commodity : Database.getInstance().getCommodities()){
            System.out.println(commodity.getId());
        }
        System.out.println("Comments:");
        for(Comment comment : Database.getInstance().getComments()){
            System.out.println(comment.getText());
        }
    }
}

