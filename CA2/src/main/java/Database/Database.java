package Database;

import Baloot.Comment;
import Baloot.Commodity;
import Baloot.Provider;
import Baloot.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Database {
    private List<User> users = new ArrayList<>();
    private List<Commodity> commodities = new ArrayList<>();
    private List<Provider> providers = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    public void addUser(User user){
        users.add(user);
    }

    public void addCommodity(Commodity commodity){
        commodities.add(commodity);
    }

    public void addProvider(Provider provider){
        providers.add(provider);
    }
}
