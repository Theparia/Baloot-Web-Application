package Model;

import Model.Id.UsedDiscountId;
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
@Table(name = "used_discount")
@IdClass(UsedDiscountId.class)
public class UsedDiscount {
    @Id
    @ManyToOne
    @JoinColumn(name = "code", referencedColumnName = "discountCode")
    private Discount discount;

    @Id
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
}
