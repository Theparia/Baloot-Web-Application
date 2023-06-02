package Domain;

import Domain.Id.ItemId;
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
@IdClass(ItemId.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Item {
    @Id
    @ManyToOne
    @JoinColumn(name = "commodityId", referencedColumnName = "id")
    private Commodity commodity;
    @Id
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
    private Integer quantity;
}
