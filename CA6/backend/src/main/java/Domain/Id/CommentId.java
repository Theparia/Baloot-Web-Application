package Domain.Id;

import Domain.Comment;
import Domain.Commodity;
import Domain.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentId implements Serializable {
    private Commodity commodity;
    private User user;

//    public CommentId(Commodity commodity, User user) {
//        this.commodity = commodity;
//        this.user = user;
//    }
}