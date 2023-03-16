package InterfaceServer;

import Baloot.*;
import Database.Database;
//import InterfaceServer.HTMLHandler;
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
        Database.getInstance().setUsers(users);

        List<Commodity> commodities = objectMapper.readValue(HTTPRequestHandler.getRequest(COMMODITIES_URI), typeFactory.constructCollectionType(List.class, Commodity.class));
        Database.getInstance().setCommodities(commodities);

        List<Provider> providers = objectMapper.readValue(HTTPRequestHandler.getRequest(PROVIDERS_URI), typeFactory.constructCollectionType(List.class, Provider.class));
        Database.getInstance().setProviders(providers);

        List<Comment> comments = objectMapper.readValue(HTTPRequestHandler.getRequest(COMMENTS_URI), typeFactory.constructCollectionType(List.class, Comment.class));
        Database.getInstance().setComments(comments);

    }

    private void runServer(final int PORT){
        Javalin app = Javalin.create().start(PORT);
        app.get("/commodities", new CommoditiesListHandler(baloot));
        app.get("/commodities/{commodity_id}", new CommodityPageHandler(baloot));
        app.get("/users/{user_id}", new UserPageHandler(baloot));
        app.post("/payment", new UserPaymentHandler(baloot));
        app.post("/addToBuyList/{commodityId}", new addToBuyListRedirectHandler(baloot));
        app.get("/addToBuyList/{username}/{commodityId}", new addToBuyListHandler(baloot));
        app.get("/removeFromBuyList/{username}/{commodityId}", new removeFromBuyListHandler(baloot));
        app.get("/providers/{provider_id}", new ProviderPageHandler(baloot));
        app.get("/addCredit/{user_id}/{credit}", new AddCreditHandler(baloot));
        app.post("/rateCommodity/{commodityId}", new rateRedirectHandler(baloot));
        app.get("/rateCommodity/{username}/{commodityId}/{rate}", new RateCommodityHandler(baloot));
        app.post("/voteComment/{vote}", new voteRedirectHandler(baloot));
        app.get("/voteComment/{username}/{commentId}/{vote}", new VoteCommentHandler(baloot));
        app.get("/commodities/search/{start_price}/{end_price}", new SearchCommoditiesByPriceHandler(baloot));
        app.get("/commodities/search/{categories}", new SearchCommoditiesByCategoryHandler(baloot));
        app.get("/200", new StatusCodePageHandler("200"));
        app.get("/403", new StatusCodePageHandler("403"));
        app.get("/404", new StatusCodePageHandler("404"));
        app.error(404, new StatusCodePageHandler("404"));
    }
}
