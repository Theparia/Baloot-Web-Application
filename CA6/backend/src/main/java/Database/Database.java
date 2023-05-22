package Database;

import Domain.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Database {
    private static Database instance = null;
    private Database(){
    }
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    private List<User> users = new ArrayList<>();
    private List<Commodity> commodities = new ArrayList<>();
    private List<Provider> providers = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    private List<Discount> discounts = new ArrayList<>();

    public void addUser(User user){
        users.add(user);
    }

    public void addCommodity(Commodity commodity){
        commodities.add(commodity);
    }

    public void addProvider(Provider provider){
        providers.add(provider);
    }

    public void addComment(Comment comment) {comments.add(comment);}

}
