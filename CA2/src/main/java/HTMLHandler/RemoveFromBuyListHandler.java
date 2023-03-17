package HTMLHandler;

import Baloot.Baloot;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class RemoveFromBuyListHandler implements Handler {
    private Baloot baloot;
    public RemoveFromBuyListHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) {
        try {
            String commodityId = ctx.pathParam("commodityId");
            String username = ctx.pathParam("username");
            baloot.removeFromBuyList(username, Integer.valueOf(commodityId));
            ctx.redirect("/200");
        }catch (Exception e){
            ctx.redirect("/403");
        }
        ctx.contentType("text/html");
    }
}
