package org.Baloot;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class Commodity {
    //@Id
    private Integer id;
    private String name; //unique validation + handling !@#
    private Integer providerId;
    private Float price;
    private List<String> categories;
    @JsonIgnore
    private HashMap<String, Float> usersRating = new HashMap<>();
    private Float rating;
    private int inStock;

    @JsonCreator
    public Commodity(@JsonProperty("id") Integer id, @JsonProperty("name") String name, @JsonProperty("providerId")  Integer providerId,
                     @JsonProperty("price")  Float price, @JsonProperty("categories")  List<String> categories,
                     @JsonProperty("rating")  Float rating, @JsonProperty("inStock")  int inStock) {
        this.id = id;
        this.name = name;
        this.providerId = providerId;
        this.price = price;
        this.categories = categories;
        this.rating = rating;
        this.inStock = inStock;
        this.usersRating.put("#initialRating#", rating);
    }

    public Boolean isInStock(){
        return inStock > 0;
    }
    public void addUserRating(String username, Integer rating){
        if (usersRating.containsKey(username)){
            usersRating.replace(username, (float) rating);
        }
        else {
            usersRating.put(username, (float) rating);
        }
        updateRating();
    }

    private void updateRating(){
        float sum = 0;
        for (HashMap.Entry<String, Float> entry : usersRating.entrySet()) {
            sum += entry.getValue();
        }
        rating = sum / usersRating.size();
    }

    public boolean isEqual(Integer id) {
        return this.id.equals(id);
    }

    public boolean isInCategory(String category){
        for(String cat: categories){
            if (cat.equals(category)){
                return true;
            }
        }
        return false;
    }

    public void reduceInStock(){
        inStock -= 1;
    }

}
