package Domain.Id;

import Domain.Discount;
import Domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UsedDiscountId implements Serializable {
    private User user;
    private Discount discount;
}
