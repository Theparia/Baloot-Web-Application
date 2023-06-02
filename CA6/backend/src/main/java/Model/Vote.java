package Model;

//import Domain.Id.CommentId;
import Model.Id.VoteId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "vote")
@IdClass(VoteId.class)
public class Vote {
    @Id
    @ManyToOne
    @JoinColumn(name = "commodity_id", referencedColumnName = "id")
    private Commodity commodity;

    @Id
    @ManyToOne
    @JoinColumn(name = "commentWriter", referencedColumnName = "username")
    private User commentWriter;

    @Id
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
    private Integer value;

    public Vote(User user, Commodity commodity, User commentWriter, Integer value){
        this.user = user;
        this.commodity = commodity;
        this.commentWriter = commentWriter;
        this.value = value;
    }

}
