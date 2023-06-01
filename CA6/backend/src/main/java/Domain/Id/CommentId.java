package Domain.Id;

import Domain.Comment;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class CommentId implements Serializable {
    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "commodity_id")
    private Integer commodityId;

//    @Column(name = "user_email")
//    private String userEmail;
//    @Column(name = "commodity_id")
//    private Integer commodityId;

    // constructors, getters, and setters

    public CommentId(String userEmail, Integer commodityId){
        this.userEmail = userEmail;
        this.commodityId = commodityId;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentId)) return false;
        CommentId that = (CommentId) o;
        return Objects.equals(getUserEmail(), that.getUserEmail()) &&
                Objects.equals(getCommodityId(), that.getCommodityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserEmail(), getCommodityId());
    }
}