package org.Baloot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Baloot {
    List<User> users = new ArrayList<>();
    List<Commodity> commodities = new ArrayList<>();
    List<Provider> providers = new ArrayList<>();
    ObjectMapper mapper;
    ObjectNode responseNode;

    public Baloot(){
        mapper = new ObjectMapper();
        responseNode = mapper.createObjectNode();
    }

    private User findUserByUsername(String username){
        for (User user : users){
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }
    private boolean doesUserExist(User newUser) {
        for (User user : users)
            if (user.isEqual(newUser.getUsername()))
                return true;
        return false;
    }

    private boolean doesProviderExist(Provider newProvider) {
        for (Provider provider : providers)
            if (provider.isEqual(newProvider.getId()))
                return true;
        return false;
    }

    public Response addUser(User newUser){
        boolean success = true;
        if (doesUserExist(newUser)) {
            findUserByUsername(newUser.getUsername()).update(newUser);
        }
        else {
            if (newUser.getUsername().matches("\\w+")) {
                users.add(newUser);
                responseNode.set("Response", mapper.convertValue("User Added.", JsonNode.class));
            }
            else{
                responseNode.set("Error", mapper.convertValue("Invalid Username", JsonNode.class));
                success = false;
            }

        }
        return new Response(success, responseNode);
        //TODO: Handling errors of Adding User
    }
     public Response addProvider(Provider newProvider) {
         if (!doesProviderExist(newProvider)){
            providers.add(newProvider);
            responseNode.set("Response", mapper.convertValue("Provider Added.", JsonNode.class));
            return new Response(true, responseNode);
         }
         else {
            responseNode.set("Response", mapper.convertValue("Provider Already Exists.", JsonNode.class));
            return new Response(false, responseNode);
         }
     }

     public Provider findProviderById(Integer providerId){
        for (Provider provider : providers){
            if(provider.isEqual(providerId)){
                return provider;
            }
        }
        return null;
     }

    public Commodity findCommodityById(Integer commodityId){
        for (Commodity commodity : commodities){
            if(commodity.isEqual(commodityId)){
                return commodity;
            }
        }
        return null;
    }

    public List<Commodity> findCommoditiesByCategory(String category){
        List<Commodity> commoditiesInCategory = new ArrayList<>();
        for (Commodity commodity : commodities){
            if(commodity.isInCategory(category)){
                commoditiesInCategory.add(commodity);
            }
        }
        return commoditiesInCategory;
    }

     public Response addCommodity(Commodity newCommodity){
        if(findProviderById(newCommodity.getProviderId()) != null){
            commodities.add(newCommodity);
            responseNode.set("Response", mapper.convertValue("Commodity Added.", JsonNode.class));
            return new Response(true, responseNode);
        }
        else {
            responseNode.set("Response", mapper.convertValue("Provider NOT Exists.", JsonNode.class));
            return new Response(false, responseNode);
        }
        //TODO: What if commodity already exist?
     }

     public Response getCommoditiesList() throws Exception{
         ObjectMapper objectMapper = new ObjectMapper();
         List<ObjectNode> JsonCommodities = new ArrayList<>();
         for (Commodity entry : commodities) {
             ObjectNode node = objectMapper.convertValue(entry, ObjectNode.class);
             JsonCommodities.add(node);
         }
         ArrayNode arrayNode = objectMapper.valueToTree(JsonCommodities);
         ObjectNode commoditiesList = objectMapper.createObjectNode();
         commoditiesList.putArray("CommoditiesList").addAll(arrayNode);
//         String data = objectMapper.writeValueAsString(commoditiesList);
         return new Response(true, commoditiesList);
     }

     public Response rateCommodity(String username, Integer commodityId, Integer score) {
        boolean success = false;
        if(score < 1 || score > 10){
            responseNode.set("Error", mapper.convertValue("Rating Out of Range", JsonNode.class));
        }
         else if(findCommodityById(commodityId) == null){
             responseNode.set("Error", mapper.convertValue("Commodity does not exist.", JsonNode.class));
         }
         else if(findUserByUsername(username) == null) {
             responseNode.set("Error", mapper.convertValue("User does not exist.", JsonNode.class));
         }
         else {
            findCommodityById(commodityId).addUserRating(username, score);
            responseNode.set("Response", mapper.convertValue("Rating Added.", JsonNode.class));
            success = true;
        }
         return new Response(success, responseNode);
     }

     public Response addToBuyList(String username, Integer commodityId){
        if (findUserByUsername(username) == null){
            responseNode.set("Response", mapper.convertValue("User Not Exists.", JsonNode.class));
            return new Response(false, responseNode);
        }
        else if(findCommodityById(commodityId) == null){
                responseNode.set("Response", mapper.convertValue("Commodity Not Exists.", JsonNode.class));
                return new Response(false, responseNode);
            }
        else if(!findCommodityById(commodityId).isInStock()){
            responseNode.set("Response", mapper.convertValue("Commodity Out of Stock.", JsonNode.class));
            return new Response(false, responseNode);
        }
        else {
            try {
                findUserByUsername(username).addToBuyList(findCommodityById(commodityId));
                findCommodityById(commodityId).reduceInStock();
                responseNode.set("Response", mapper.convertValue("Commodity Added to buyList", JsonNode.class));
                return new Response(true, responseNode);
            } catch (Exception e){
                responseNode.set("Response", mapper.convertValue(e.getMessage(), JsonNode.class));
                return new Response(false, responseNode);
            }
        }

     }

     public Response removeFromBuyList(String username, Integer commodityId){
         if (findUserByUsername(username) == null){
             responseNode.set("Response", mapper.convertValue("User Not Exists.", JsonNode.class));
             return new Response(false, responseNode);
         }
         else{
             try{
                 findUserByUsername(username).removeFromBuyList(findCommodityById(commodityId));
                 responseNode.set("Response", mapper.convertValue("Commodity removed from buyList.", JsonNode.class));
                 return new Response(true, responseNode);
             } catch (Exception e){
                 responseNode.set("Response", mapper.convertValue(e.getMessage(), JsonNode.class));
                 return new Response(false, responseNode);
             }
         }
     }

    public Response getCommodityById(Integer commodityId){
        if(findCommodityById(commodityId) == null){
            responseNode.set("Response", mapper.convertValue("Commodity Not Exists.", JsonNode.class));
            return new Response(false, responseNode);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.convertValue(findCommodityById(commodityId), ObjectNode.class);
        return new Response(true, node);
    }

    public Response getCommoditiesByCategory(String category){
        List<Commodity> commoditiesInCategory = findCommoditiesByCategory(category);
        ObjectMapper objectMapper = new ObjectMapper();
        List<ObjectNode> JsonCommodities = new ArrayList<>();
        for (Commodity entry : commoditiesInCategory) {
            ObjectNode node = objectMapper.convertValue(entry, ObjectNode.class);
            JsonCommodities.add(node);
        }
        ArrayNode arrayNode = objectMapper.valueToTree(JsonCommodities);
        ObjectNode commoditiesList = objectMapper.createObjectNode();
        commoditiesList.putArray("commoditiesListByCategory").addAll(arrayNode);
        return new Response(true, commoditiesList);
        // TODO: omit inStock field for just this command.
        // TODO: Is the sample output correct?
    }

    public Response getBuyList(String username){
        if (findUserByUsername(username) == null){
            responseNode.set("Response", mapper.convertValue("User Not Exists.", JsonNode.class));
            return new Response(false, responseNode);
        }
        List<Commodity> buyList = findUserByUsername(username).getBuyList();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ObjectNode> JsonCommodities = new ArrayList<>();
        for (Commodity entry : buyList) {
            ObjectNode node = objectMapper.convertValue(entry, ObjectNode.class);
            JsonCommodities.add(node);
        }
        ArrayNode arrayNode = objectMapper.valueToTree(JsonCommodities);
        ObjectNode commoditiesList = objectMapper.createObjectNode();
        commoditiesList.putArray("buyList").addAll(arrayNode);
        return new Response(true, commoditiesList);
        //TODO: is omitting categories & inStock fields needed?
    }

    public void printData(){
        System.out.println("Users:");
        for(User user : users){
            System.out.println(user.getUsername());
        }
        System.out.println("Providers:");
        for(Provider provider : providers){
            System.out.println(provider.getId());
        }
        System.out.println("Commodities:");
        for(Commodity commodity : commodities){
            System.out.println(commodity.getId());
        }
    }

}

