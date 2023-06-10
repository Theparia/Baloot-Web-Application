package Model.Id;

import Model.Commodity;
import Model.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class VoteId implements Serializable{
    private User user;
    private Commodity commodity;
    private User commentWriter;
}
