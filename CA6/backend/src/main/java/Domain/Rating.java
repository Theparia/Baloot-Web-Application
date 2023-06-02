package Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rating")
public class Rating {
    @Id  //TODO: composite key
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String username; //TODO: foreign key
    private Integer commodityId; //TODO: foreign key
    private float score;

    public Rating(String username, Integer commodityId, float score){
        this.username = username;
        this.commodityId = commodityId;
        this.score = score;
    }
}
