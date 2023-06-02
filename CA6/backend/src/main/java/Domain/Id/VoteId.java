package Domain.Id;

import Domain.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class VoteId implements Serializable{
    private User user;
    private Commodity commodity;
    private User commentWriter;
}
