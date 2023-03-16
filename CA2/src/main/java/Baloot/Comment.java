package Baloot;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class Comment {
    private static int idGenerator = 0;
    private Integer id;
    private Integer commodityId;
    private String userEmail;
    private String text;
    private String date;
    private HashMap<Integer, List<String>> votes = new HashMap<Integer, List<String>>(){{
        put(1, new ArrayList<>());
        put(0, new ArrayList<>());
        put(-1, new ArrayList<>());
    }}; // <1, 0, -1> ---> [usernames]

    public Comment(Integer commodityId, String userEmail, String text, String date){
        this.id = idGenerator++;
        this.date = date;
        this.commodityId = commodityId;
        this.text = text;
        this.userEmail = userEmail;
    }

    public Comment(){
        this.id = idGenerator++;
    }

    public void addVote(String username, Integer vote){
        votes.get(vote).add(username);
    }

    public Integer getLikeCount(){
        return votes.get(1).size();
    }
    public Integer getDisLikeCount(){
        return votes.get(-1).size();
    }
}
