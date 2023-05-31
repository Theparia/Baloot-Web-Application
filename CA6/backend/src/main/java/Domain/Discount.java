package Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "discount")
public class Discount {
    @Id
    private String discountCode;
    private float discount;

//    public Discount(String discountCode, float discount){
//        this.discount = discount;
//        this.discountCode = discountCode;
//    }
}
