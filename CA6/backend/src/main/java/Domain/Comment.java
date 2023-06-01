package Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String userEmail;
//    private Integer commodityId;
    private String text;
    private String date;
//    private String username;
    private Integer likeCount = 0;
    private Integer dislikeCount = 0;
//    @JsonIgnore
//    private HashMap<String, Integer> votes = new HashMap<String, Integer>(); // username ---> {-1, 0, 1}

    @JsonIgnore
    @OneToMany
    private List<Vote> votes;

    @ManyToOne
    @JoinColumn(name = "commodityId", referencedColumnName = "id")
    private Commodity commodity;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;


    public Comment(Integer commodityId, String userEmail, String text, String date){
//        this.id = UUID.randomUUID();
        this.date = date;
//        this.commodityId = commodityId;
        this.text = text;
        this.userEmail = userEmail;
        this.likeCount = 0;
        this.dislikeCount = 0;
    }

    public void setCommodity(Commodity commodity){
        this.commodity = commodity;
    }
//
//    public Comment(){
//        this.id = UUID.randomUUID();
//    }


//    public void addVote(String username, Integer vote){
//        if (votes.containsKey(username)){
//            if(Objects.equals(votes.get(username), vote)){
//                votes.remove(username);
//            }
//            else {
//                votes.replace(username, vote);
//            }
//        }
//        else {
//            votes.put(username, vote);
//        }
//        updateLikeDislikeCount();
//    }

//    private void updateLikeDislikeCount(){
//        likeCount = countVotes(1);
//        dislikeCount = countVotes(-1);
//    }

//    private Integer countVotes(Integer vote){
//        return Collections.frequency(votes.values(), vote);
//    }
}
