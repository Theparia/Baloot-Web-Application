package org.Baloot;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class Commodity {
    //@Id
    private String id;
    private String name; //unique validation + handling !@#
    private String providerId;
    private float price;
    private List<String> categories;
    private int rating;
    private int inStock;
    public Commodity(String id, String name, String providerId, float price, List<String> categories, int rating, int inStock) {
        this.id = id;
        this.name = name;
        this.providerId = providerId;
        this.price = price;
        this.categories = categories;
        this.rating = rating;
        this.inStock = inStock;
    }

//    public void reduceInStock(int count){
//        if(inStock - count < 0){
//            throw new Exception("Error: Out of Stock.");
//        }
//        else {
//            inStock -= count;
//        }
//    }

}
