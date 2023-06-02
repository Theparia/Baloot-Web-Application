package Domain;


import Exceptions.CommodityOutOfStock;
import Exceptions.RatingOutOfRange;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "commodity")
public class Commodity {
    @Id
    private Integer id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "providerId", referencedColumnName = "id")
    private Provider provider;

    private Float price;

    @Column
    @ElementCollection(targetClass=String.class)
    private List<String> categories; //TODO: Category class?

    @JsonIgnore
    @Column
    @ElementCollection(targetClass=Float.class)
    private List<Float> userRatings; //TODO: add initial rating

    private Float rating;
    private int inStock;
    private String image;

    public void checkInStock(Integer quantity) throws CommodityOutOfStock {
        if(inStock < quantity)
            throw new CommodityOutOfStock(); //TODO: name?
    }
//    public void addUserRating(String username, Integer rating) throws RatingOutOfRange {
//        if(rating < 1 || rating > 10)
//            throw new RatingOutOfRange();
//        if (usersRating.containsKey(username)){
//            usersRating.replace(username, (float) rating);
//        }
//        else {
//            usersRating.put(username, (float) rating);
//        }
//        updateRating();
//    }

//    private void updateRating(){
//        float sum = 0;
//        for (HashMap.Entry<String, Float> entry : usersRating.entrySet()) {
//            sum += entry.getValue();
//        }
//        rating = sum / usersRating.size();
//    }

    public void updateRating(){
        float sum = 0.0f;
        for (Float score : userRatings) {
            sum += score;
        }
        this.rating = sum / userRatings.size();
    }

    public boolean isEqual(Integer id) {
        return this.id.equals(id);
    }

    public boolean isInCategory(String category){
        return categories.contains(category);
    }

    public boolean isInSimilarCategory(List<String> categories_) {
        return categories.stream().anyMatch(categories_::contains);
    }

    public void reduceInStock(Integer quantity) throws CommodityOutOfStock {
        if(inStock < quantity)
            throw new CommodityOutOfStock();
        inStock -= quantity;
    }

}
