package Domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "purchasedListItem")
public class PurchasedListItem extends Item{
    public PurchasedListItem(Commodity commodity, User user, Integer quantity) {
        super(commodity, user, quantity);
    }
}
