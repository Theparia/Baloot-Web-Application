package DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommodityDTO {
    private Integer id;
    private String name;
    private Integer providerId;
    private String providerName;
    private Float price;
    private List<String> categories;
    private Float rating;
    private int inStock;
    private String image;
    private List<Float> userRatings;
}
