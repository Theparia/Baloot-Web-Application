package Domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Discount {
    private String discountCode;
    private float discount;
    public Discount(String discountCode, float discount){
        this.discount = discount;
        this.discountCode = discountCode;
    }
}
