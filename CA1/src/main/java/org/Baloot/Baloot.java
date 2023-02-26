package org.Baloot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Baloot {
    List<User> users = new ArrayList<>();
    List<Commodity> commodities = new ArrayList<>();
    List<Provider> providers = new ArrayList<>();

    private User findUserByUsername(String username){
        for (User user : users){
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }
    private boolean doesUserExist(User newUser) {
        for (User user : users)
            if (user.isEqual(newUser.getUsername()))
                return true;
        return false;
    }

    public void addUser(User newUser){
        if (doesUserExist(newUser)) {
            System.out.println(newUser.getUsername());
            findUserByUsername(newUser.getUsername()).update(newUser);
        }
        else {
            users.add(newUser);
        }
    }
}

