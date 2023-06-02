package Model.Id;

import Model.Discount;
import Model.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UsedDiscountId implements Serializable {
    private User user;
    private Discount discount;
}
