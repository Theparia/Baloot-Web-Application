package Domain;


import Exceptions.CommodityNotInBuyList;
import Exceptions.ExpiredDiscount;
import Exceptions.NegativeCredit;
import Exceptions.NotEnoughCredit;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    private String username;
    private String password;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-DD")
    private String birthDate;
    private String address;
    private float credit;
//    @JsonIgnore
//    @OneToMany
//    private List<BuyListItem> buyList = new ArrayList<>();
//    @JsonIgnore
//    @OneToMany
//    private List<BuyListItem> purchasedList = new ArrayList<>();
//    @JsonIgnore
//    private HashMap<Integer, Integer> buyList = new HashMap<>(); // id -> quantity
//    @JsonIgnore
//    private HashMap<Integer, Integer> purchasedList = new HashMap<>(); // id -> quantity
    @JsonIgnore
    @Transient
    private List<Discount> usedDiscounts = new ArrayList<>();
    @JsonIgnore
    @Transient
    private Discount currentDiscount = null;
    public User(String username, String password, String email, String birthDate, String address, float credit) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.credit = credit;
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
//        this.buyList = user.getBuyList();
        this.usedDiscounts = user.getUsedDiscounts();
        this.currentDiscount = null;
    }

//    public float getCurrentBuyListPrice(){
//        float totalPrice = 0;
//
//        for (var entry : this.buyList.entrySet()) {
//            totalPrice += entry.getValue() * entry.getKey().getPrice();
//        }
//
//        if (this.currentDiscount == null) return totalPrice;
//        return totalPrice * (100 - this.currentDiscount.getDiscount()) / 100;
//    }

    public void setCurrentDiscount(Discount discount) throws ExpiredDiscount {
        if (isDiscountCodeExpired(discount.getDiscountCode())){
            throw new ExpiredDiscount();
        }
        this.currentDiscount = discount;
    }

    public void useDiscount(){
        if(this.currentDiscount != null)
            this.usedDiscounts.add(this.currentDiscount);
        this.currentDiscount = null;
    }

    public void deleteCurrentDiscount(){
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
//        this.buyList = new HashMap<>();
    }

    public void reduceCredit(float amount) throws NotEnoughCredit {
        if (this.credit - amount < 0 ){
            throw new NotEnoughCredit();
        }
        this.credit -= amount;
    }

    public void addToBuyList(Integer commodityId) {
//        if(buyList.containsKey(commodityId))
//            buyList.replace(commodityId, buyList.get(commodityId) + 1);
//        else
//            buyList.put(commodityId, 1);
    }

    public void addToPurchasedList(Integer commodityId) {
//        if(purchasedList.containsKey(commodityId))
//            purchasedList.replace(commodityId, buyList.get(commodityId) + purchasedList.get(commodityId));
//        else
//            purchasedList.put(commodityId, buyList.get(commodityId));
    }

    public void removeFromBuyList(Integer commodityId) throws CommodityNotInBuyList {
//        if(!buyList.containsKey(commodityId))
//            throw new CommodityNotInBuyList();
//        if(buyList.get(commodityId) == 1)
//            buyList.remove(commodityId);
//        else
//            buyList.replace(commodityId, buyList.get(commodityId) - 1);
    }

//    public void addCredit(float creditToBeAdded) throws NegativeCredit {
//        if(creditToBeAdded <= 0)
//            throw new NegativeCredit();
//        credit += creditToBeAdded;
//    }

//    public int getNumberOfBuyListItems(){
//        return buyList.size();
//    }
}
