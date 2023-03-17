package HTMLHandler;

import Baloot.Baloot;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class AddToBuyListHandler implements Handler {
    private Baloot baloot;
    public AddToBuyListHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) {
        try {
            String method = String.valueOf(ctx.method());
            String commodityId = ctx.pathParam("commodityId");
            String userId = "";
            if (method.equals("GET")) {
                userId = ctx.pathParam("username");
            } else if (method.equals("POST")) {
                userId = ctx.formParam("userId");
            }
            baloot.addToBuyList(userId, Integer.valueOf(commodityId));
            ctx.redirect("/200");
        } catch (Exception e){
            ctx.redirect("/403");
        }
        ctx.contentType("text/html");
    }
}