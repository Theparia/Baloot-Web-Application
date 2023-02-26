package org.Baloot;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
//@NoArgsConstructor
public class User {
    private int id;
    private String username; //unique validation + handling !@#
    private String password;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-DD")
    private Date birthDate;
    private String address;
    private float credit;
    public User(int id, String username, String password, String email, Date birthDate, String address, float credit) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.credit = credit;
    }
}
