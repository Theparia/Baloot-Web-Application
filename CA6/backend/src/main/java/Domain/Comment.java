package Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Comment {
    @JsonIgnore
    private static int idGenerator = 0;

    private UUID id;
    private Integer commodityId;
    private String userEmail;
    private String text;
    private String date;
    private String username;
    private Integer likeCount = 0;
    private Integer dislikeCount = 0;
    @JsonIgnore
    private HashMap<String, Integer> votes = new HashMap<String, Integer>(); // username ---> {-1, 0, 1}

    public Comment(Integer commodityId, String userEmail, String text, String date){
        this.id = UUID.randomUUID();
        this.date = date;
        this.commodityId = commodityId;
        this.text = text;
        this.userEmail = userEmail;
        this.likeCount = 0;
        this.dislikeCount = 0;
    }

    public Comment(){
        this.id = UUID.randomUUID();
    }

    public void addVote(String username, Integer vote){
        if (votes.containsKey(username)){
            if(Objects.equals(votes.get(username), vote)){
                votes.remove(username);
            }
            else {
                votes.replace(username, vote);
            }
        }
        else {
            votes.put(username, vote);
        }
        updateLikeDislikeCount();
    }

    private void updateLikeDislikeCount(){
        likeCount = countVotes(1);
        dislikeCount = countVotes(-1);
    }

    private Integer countVotes(Integer vote){
        return Collections.frequency(votes.values(), vote);
    }
}
