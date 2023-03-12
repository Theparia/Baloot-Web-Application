package InterfaceServer;

import Baloot.Baloot;
import io.javalin.Javalin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private void importBalootDatabase(final String USERS_URI, final String COMMODITIES_URI, final String PROVIDERS_URI, final String COMMENTS_URI){
//        baloot.setUsers();
    }

    private void runServer(final int PORT){

    }

}
