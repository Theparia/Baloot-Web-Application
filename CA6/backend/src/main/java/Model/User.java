package Model;


import Exceptions.ExpiredDiscount;
import Exceptions.NotEnoughCredit;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-DD")
    private String birthDate;
    private String address;
    private float credit;
    @JsonIgnore
    @OneToOne
    private Discount currentDiscount = null;
    public User(String username, String password, String email, String birthDate, String address, float credit) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.credit = credit;
    }

    public void setCurrentDiscount(Discount discount) throws ExpiredDiscount {
        this.currentDiscount = discount;
        System.out.println("!!!: " + this.currentDiscount.getDiscountCode());
    }

    public void useDiscount(){
        this.currentDiscount = null;
    }

    public void deleteCurrentDiscount(){
        this.currentDiscount = null;
    }


    public void reduceCredit(float amount) throws NotEnoughCredit {
        if (this.credit - amount < 0 ){
            throw new NotEnoughCredit();
        }
        this.credit -= amount;
    }
}
