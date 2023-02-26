package org.Baloot;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class User {
    private String username; //unique validation + handling !@#
    private String password;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-DD")
    private Date birthDate;
    private String address;
    private float credit;

    List<Commodity> BuyList;
    public User(String username, String password, String email, Date birthDate, String address, float credit) {
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
        this.BuyList = user.getBuyList();
    }
}
