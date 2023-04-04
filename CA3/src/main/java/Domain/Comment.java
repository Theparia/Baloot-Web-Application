package Domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
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

    private HashMap<String, Integer> votes = new HashMap<String, Integer>(); // username ---> {-1, 0, 1}

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
        if (votes.containsKey(username)){
            votes.replace(username, vote);
        }
        else {
            votes.put(username, vote);
        }
    }

    private Integer countVotes(Integer vote){
        return Collections.frequency(votes.values(), vote);
    }

    public Integer getLikeCount(){
        return countVotes(1);
    }
    public Integer getDisLikeCount(){
        return countVotes(-1);
    }
}
