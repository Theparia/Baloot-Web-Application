package Domain.Id;

import Domain.Commodity;
import Domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyListId implements Serializable{
    private Commodity commodity;
    private User user;

}
