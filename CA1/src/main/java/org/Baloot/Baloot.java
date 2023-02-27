package org.Baloot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


import java.util.ArrayList;
import java.util.List;

public class Baloot {
    List<User> users = new ArrayList<>();
    List<Commodity> commodities = new ArrayList<>();
    List<Provider> providers = new ArrayList<>();

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

    public void addUser(User newUser){
        if (doesUserExist(newUser)) {
            System.out.println(newUser.getUsername());
            findUserByUsername(newUser.getUsername()).update(newUser);
        }
        else {
            users.add(newUser);
        }
    }
     public void addProvider(Provider newProvider) throws Exception{
        if(!doesProviderExist(newProvider)){
            providers.add(newProvider);
        }
        else{
            throw new Exception("Provider with id "+ newProvider.getId() + " already exist.");
        }
     }

     public Provider findProviderById(String providerId){
        for (Provider provider : providers){
            if(provider.isEqual(providerId)){
                return provider;
            }
        }
        return null;
     }

     public void addCommodity(Commodity newCommodity) throws Exception{
        if(findProviderById(newCommodity.getProviderId()) != null){
            commodities.add(newCommodity);
        }
        else throw new Exception("Error: Provider with id " + newCommodity.getProviderId() + " does not exists.");
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

