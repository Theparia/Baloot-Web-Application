package Baloot;

import Baloot.Exceptions.*;
import Database.Database;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

enum ResponseType{
    RESPONE,
    ERROR
}
@Getter
@Setter
public class Baloot { //todo db public or private?
//    private Database db = new Database();
    private ObjectMapper mapper;
    private ObjectNode responseNode;

    public Baloot(){
        mapper = new ObjectMapper();
        responseNode = mapper.createObjectNode();
    }

    public void setResponseNode(ResponseType responseType, String message){
        String type = (responseType == ResponseType.RESPONE) ? "Response" : "Error";
        responseNode.set(type, mapper.convertValue(message, JsonNode.class));
    }

    public User findUserByUsername(String username) throws UserNotFound {
        for (User user : Database.getInstance().getUsers()){
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        throw new UserNotFound();
    }
    private boolean doesUserExist(User newUser) {
        for (User user : Database.getInstance().getUsers())
            if (user.isEqual(newUser.getUsername()))
                return true;
        return false;
    }

    private boolean doesProviderExist(Provider newProvider) {
        for (Provider provider : Database.getInstance().getProviders())
            if (provider.isEqual(newProvider.getId()))
                return true;
        return false;
    }

    public Response addUser(User newUser){
        boolean success = true;
        try{
            findUserByUsername(newUser.getUsername()).update(newUser);
            setResponseNode(ResponseType.RESPONE, "User Info Updated");
        } catch (UserNotFound userNotFoundError){
             try{
                 if(!newUser.getUsername().matches("\\w+"))
                     throw new InvalidUsername(); //todo
                 Database.getInstance().addUser(newUser);
                 setResponseNode(ResponseType.RESPONE, "User Added");
             } catch (InvalidUsername invalidUsernameError){
                 setResponseNode(ResponseType.ERROR, invalidUsernameError.getMessage());
                 success = false;
             }
        }
        return new Response(success, responseNode);
    }
     public Response addProvider(Provider newProvider) {
        boolean success = true;
        try{
            findProviderById(newProvider.getId());
            setResponseNode(ResponseType.ERROR, new ProviderAlreadyExists().getMessage()); //todo: mikhad asan ino?
            success = false;
        } catch (ProviderNotFound providerNotFoundError){
            Database.getInstance().addProvider(newProvider);
            setResponseNode(ResponseType.RESPONE, "Provider Added");
        }
        return new Response(success, responseNode);
     }

     public Provider findProviderById(Integer providerId) throws ProviderNotFound {
        for (Provider provider : Database.getInstance().getProviders()){
            if(provider.isEqual(providerId)){
                return provider;
            }
        }
        throw new ProviderNotFound();
     }

    public Commodity findCommodityById(Integer commodityId) throws CommodityNotFound {
        for (Commodity commodity : Database.getInstance().getCommodities()){
            if(commodity.isEqual(commodityId)){
                return commodity;
            }
        }
        throw new CommodityNotFound();
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

     public Response addCommodity(Commodity newCommodity){
        boolean success = true;
        try{
            findProviderById(newCommodity.getProviderId());
            Database.getInstance().addCommodity(newCommodity);
            setResponseNode(ResponseType.RESPONE, "Commodity Added.");
        } catch (ProviderNotFound providerNotFoundError){
            setResponseNode(ResponseType.ERROR, providerNotFoundError.getMessage());
            success = false;
        }
        return new Response(success, responseNode);
     }

     public Response getCommoditiesList() throws Exception{
         ObjectMapper objectMapper = new ObjectMapper();
         List<ObjectNode> JsonCommodities = new ArrayList<>();
         for (Commodity entry : Database.getInstance().getCommodities()) {
             ObjectNode node = objectMapper.convertValue(entry, ObjectNode.class);
             JsonCommodities.add(node);
         }
         ArrayNode arrayNode = objectMapper.valueToTree(JsonCommodities);
         ObjectNode commoditiesList = objectMapper.createObjectNode();
         commoditiesList.putArray("CommoditiesList").addAll(arrayNode);
         return new Response(true, commoditiesList);
     }

//     public Response rateCommodity(String username, Integer commodityId, Integer score) {
//        boolean success = true;
//        try {
//            findUserByUsername(username);
//            findCommodityById(commodityId).addUserRating(username, score);
//            setResponseNode(ResponseType.RESPONE, "Rating Added");
//        } catch (Exception e){
//            setResponseNode(ResponseType.ERROR, e.getMessage());
//            success = false;
//        }
//         return new Response(success, responseNode);
//     }


     public void addToBuyList(String username, Integer commodityId) throws CommodityNotFound, UserNotFound, CommodityOutOfStock, CommodityAlreadyExistsInBuyList {
        findCommodityById(commodityId).checkInStock();
        findUserByUsername(username).addToBuyList(findCommodityById(commodityId));
     }

    public void finalizePayment(String userId) throws UserNotFound, CommodityOutOfStock, NotEnoughCredit {
        User user = findUserByUsername(userId);
        List<Commodity> buyListCommodities = user.getBuyList();
        for (Commodity commodity : buyListCommodities){
            commodity.checkInStock();
        }
        for (Commodity commodity : buyListCommodities){
            user.reduceCredit(commodity.getPrice());
            user.addToPurchasedList(commodity);
            commodity.reduceInStock();
        }
        user.resetBuyList();
    }

     public void removeFromBuyList(String username, Integer commodityId) throws UserNotFound, CommodityNotFound, CommodityNotInBuyList {
            findUserByUsername(username).removeFromBuyList(findCommodityById(commodityId));
     }

     public List<Comment> getCommodityComments(String commodityId){
        List<Comment> comments = new ArrayList<>();
        for (Comment comment : Database.getInstance().getComments()){
            if (String.valueOf(comment.getCommodityId()).equals(commodityId)){
                comments.add(comment);
            }
        }
        return comments;
     }

     public String getUsernameByEmail(String email){
        for (User user : Database.getInstance().getUsers()){
            if (user.getEmail().equals(email)){
                return user.getUsername();
            }
        }
        return "";
     }

    public Response getCommodityById(Integer commodityId){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.convertValue(findCommodityById(commodityId), ObjectNode.class);
            return new Response(true, node);
        } catch (Exception e){
            setResponseNode(ResponseType.ERROR, e.getMessage());
            return new Response(false, responseNode);
        }
    }

    public Response getCommoditiesByCategory(String category){
        List<Commodity> commoditiesInCategory = findCommoditiesByCategory(category);
        ObjectMapper objectMapper = new ObjectMapper();
        List<ObjectNode> JsonCommodities = new ArrayList<>();
        for (Commodity entry : commoditiesInCategory) {
            ObjectNode node = objectMapper.convertValue(entry, ObjectNode.class);
            node.remove("inStock");
            JsonCommodities.add(node);
        }
        ArrayNode arrayNode = objectMapper.valueToTree(JsonCommodities);
        ObjectNode commoditiesList = objectMapper.createObjectNode();
        commoditiesList.putArray("commoditiesListByCategory").addAll(arrayNode);
        return new Response(true, commoditiesList);
    }

    public Response getBuyList(String username){
        try{
            List<Commodity> buyList = findUserByUsername(username).getBuyList();
            ObjectMapper objectMapper = new ObjectMapper();
            List<ObjectNode> JsonCommodities = new ArrayList<>();
            for (Commodity entry : buyList) {
                ObjectNode node = objectMapper.convertValue(entry, ObjectNode.class);
                node.remove("inStock");
                JsonCommodities.add(node);
            }
            ArrayNode arrayNode = objectMapper.valueToTree(JsonCommodities);
            ObjectNode commoditiesList = objectMapper.createObjectNode();
            commoditiesList.putArray("buyList").addAll(arrayNode);
            return new Response(true, commoditiesList);
        } catch (Exception e){
            setResponseNode(ResponseType.ERROR, e.getMessage());
            return new Response(false, responseNode);
        }
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
        if(!isVoteValid(vote)) //todo: existing like/dislike
            throw new InvalidCommentVote();
        findCommentById(commentId).addVote(findUserByUsername(username).getUsername(), vote);
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

