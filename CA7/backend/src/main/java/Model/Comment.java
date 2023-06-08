package Model;

//import Domain.Id.CommodityUserId;
import Model.Id.CommodityUserId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment")
@IdClass(CommodityUserId.class)
public class Comment {

    private String userEmail;
    private String text;
    private String date;
    private Integer likeCount = 0;
    private Integer dislikeCount = 0;
    @Id
    @ManyToOne
    @JoinColumn(name = "commodityId", referencedColumnName = "id")
    private Commodity commodity;

    @Id
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    public Comment(User user, Commodity commodity, String userEmail, String text, String date){
        this.user = user;
        this.commodity = commodity;
        this.date = date;
        this.text = text;
        this.userEmail = userEmail;
        this.likeCount = 0;
        this.dislikeCount = 0;
    }

    public void setCommodity(Commodity commodity){
        this.commodity = commodity;
    }
}
