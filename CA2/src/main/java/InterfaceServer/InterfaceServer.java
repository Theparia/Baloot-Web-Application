package InterfaceServer;

import Baloot.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.javalin.Javalin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import HTTPRequestHandler.HTTPRequestHandler;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class InterfaceServer {
    private Baloot baloot = new Baloot();
    private Javalin app;

    public void start(final String USERS_URI, final String COMMODITIES_URI, final String PROVIDERS_URI, final String COMMENTS_URI, final int PORT){
        try {
            importBalootDatabase(USERS_URI, COMMODITIES_URI, PROVIDERS_URI, COMMENTS_URI);
            runServer(PORT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void importBalootDatabase(final String USERS_URI, final String COMMODITIES_URI, final String PROVIDERS_URI, final String COMMENTS_URI) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();

        List<User> users = objectMapper.readValue(HTTPRequestHandler.getRequest(USERS_URI), typeFactory.constructCollectionType(List.class, User.class));
        baloot.getDb().setUsers(users);

        List<Commodity> commodities = objectMapper.readValue(HTTPRequestHandler.getRequest(COMMODITIES_URI), typeFactory.constructCollectionType(List.class, Commodity.class));
        baloot.getDb().setCommodities(commodities);

        List<Provider> providers = objectMapper.readValue(HTTPRequestHandler.getRequest(PROVIDERS_URI), typeFactory.constructCollectionType(List.class, Provider.class));
        baloot.getDb().setProviders(providers);

        List<Comment> comments = objectMapper.readValue(HTTPRequestHandler.getRequest(COMMENTS_URI), typeFactory.constructCollectionType(List.class, Comment.class));
        baloot.getDb().setComments(comments);
    }

    private void runServer(final int PORT){

    }

}
