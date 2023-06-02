package Model.Id;

import Model.Commodity;
import Model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentId implements Serializable {
    private Commodity commodity;
    private User user;
}