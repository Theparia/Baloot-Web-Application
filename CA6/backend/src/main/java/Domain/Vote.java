package Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "vote")
public class Vote {
    @Id //todo: composite key after user table created
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String username; //todo: foreign key after user table created
    private Integer commentId; //todo: foreign key after user table created
    private Integer value;

    public Vote(String username, Integer commentId, Integer value){
        this.username = username;
        this.commentId = commentId;
        this.value = value;
    }
}
