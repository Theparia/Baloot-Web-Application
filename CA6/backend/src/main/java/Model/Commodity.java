package Model;


import Exceptions.CommodityOutOfStock;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private List<Float> userRatings;

    private Float rating; // Initial Rating
    private Float averageRating;
    private int inStock;
    private String image;

    public void checkInStock(Integer quantity) throws CommodityOutOfStock {
        if(inStock < quantity)
            throw new CommodityOutOfStock();
    }

    public void setUserRatings(List<Float> scores){
        userRatings = scores;
        userRatings.add(rating);
    }

    public void updateAverageRating(){
        float sum = 0.0f;
        for (Float score : userRatings) {
            sum += score;
        }
        averageRating = sum / userRatings.size();
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
