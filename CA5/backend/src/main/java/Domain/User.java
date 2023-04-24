package Domain;


import Service.Exceptions.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class User {

    private String username;
    private String password;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-DD")
    private String birthDate;
    private String address;
    private float credit;
    private List<Commodity> buyList = new ArrayList<>();
    private List<Commodity> purchasedList = new ArrayList<>();
    private List<Discount> usedDiscounts = new ArrayList<>();
    private Discount currentDiscount = null;
    public User(String username, String password, String email, String birthDate, String address, float credit) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.credit = credit;
        this.buyList = new ArrayList<>();
        this.usedDiscounts = new ArrayList<>();
        this.currentDiscount = null;
    }

    public boolean isEqual(String username) {
        return this.username.equals(username);
    }

    public void update(User user){
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.credit = user.getCredit();
        this.address = user.getAddress();
        this.birthDate = user.getBirthDate();
        this.buyList = user.getBuyList();
        this.usedDiscounts = user.getUsedDiscounts();
        this.currentDiscount = null;
    }

    public float getCurrentBuyListPrice(){
        float totalPrice = 0;
        for (Commodity commodity: this.buyList){
            totalPrice += commodity.getPrice();
        }
        if (this.currentDiscount == null) return totalPrice;
        return totalPrice * (100 - this.currentDiscount.getDiscount()) / 100;
    }

    public void setCurrentDiscount(Discount discount) throws ExpiredDiscount {
        if (isDiscountCodeExpired(discount.getDiscountCode())){
            throw new ExpiredDiscount();
        }
        this.currentDiscount = discount;
    }

    public void useDiscount(){
        this.usedDiscounts.add(this.currentDiscount);
        this.currentDiscount = null;
    }

    public boolean isDiscountCodeExpired(String code){
        for (Discount discount: this.usedDiscounts){
            if(discount.getDiscountCode().equals(code)){
                return true;
            }
        }
        return false;
    }

    public void resetBuyList(){
        this.buyList = new ArrayList<>();
    }

    public void reduceCredit(float amount) throws NotEnoughCredit {
        if (this.credit - amount < 0 ){
            throw new NotEnoughCredit();
        }
        this.credit -= amount;
    }

    public Commodity findCommodity(Commodity commodity){
        for (Commodity commodity1: buyList){
            if(commodity1.isEqual(commodity.getId())){
                return commodity1;
            }
        }
        return null;
    }

    public void addToBuyList(Commodity commodity) throws CommodityAlreadyExistsInBuyList {
        if(findCommodity(commodity) != null){
            throw new CommodityAlreadyExistsInBuyList();
        }
        this.buyList.add(commodity);
    }

    public void addToPurchasedList(Commodity commodity) {
        this.purchasedList.add(commodity);
    }

    public void removeFromBuyList(Commodity commodity) throws CommodityNotInBuyList {
        if(findCommodity(commodity) == null){
            throw new CommodityNotInBuyList();
        }
        this.buyList.remove(commodity);
    }

    public void addCredit(float creditToBeAdded) throws NegativeCredit {
        if(creditToBeAdded <= 0)
            throw new NegativeCredit();
        credit += creditToBeAdded;
    }
}
