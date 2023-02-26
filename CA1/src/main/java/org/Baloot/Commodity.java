package org.Baloot;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Commodity {
    //@Id
    private int id;
    private String name; //unique validation + handling !@#
    private String providerID;
    private float price;
    private List<String> categories;
    private int rating;
    private int inStock;
    public Commodity(int id, String name, String providerID, float price, List<String> categories, int rating, int inStock) {
        this.id = id;
        this.name = name;
        this.providerID = providerID;
        this.price = price;
        this.categories = categories;
        this.rating = rating;
        this.inStock = inStock;
    }

}
