package Model;

import Model.Id.CommodityUserId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rating")
@IdClass(CommodityUserId.class)
public class Rating {
    @Id
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "commodityId", referencedColumnName = "id")
    private Commodity commodity;

    private float score;
}
