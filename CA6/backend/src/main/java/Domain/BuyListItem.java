package Domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "buyListItem")
public class BuyListItem extends Item{
    public BuyListItem(Commodity commodity, User user, Integer quantity) {
        super(commodity, user, quantity);
    }
}
