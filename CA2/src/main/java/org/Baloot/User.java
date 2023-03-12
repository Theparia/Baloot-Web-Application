package org.Baloot;


import Exceptions.CommodityAlreadyExistsInBuyList;
import Exceptions.CommodityNotInBuyList;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-DD")
    private Date birthDate;
    private String address;
    private float credit;
    List<Commodity> buyList = new ArrayList<>();
    public User(String username, String password, String email, Date birthDate, String address, float credit) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.credit = credit;
        this.buyList = new ArrayList<>();
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

    public void removeFromBuyList(Commodity commodity) throws CommodityNotInBuyList {
        if(findCommodity(commodity) == null){
            throw new CommodityNotInBuyList();
        }
        this.buyList.remove(commodity);
    }

}
