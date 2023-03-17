package HTMLHandler;

import Baloot.Baloot;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class RateCommodityHandler implements Handler {
    Baloot baloot;
    public RateCommodityHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        try {
            String username = ctx.pathParam("username");
            Integer commodityId = Integer.valueOf(ctx.pathParam("commodityId"));
            Integer rate = Integer.valueOf(ctx.pathParam("rate"));
            baloot.rateCommodity(username, commodityId, rate);
            ctx.redirect("/200");
        } catch (Exception e){
            System.out.println(e.getMessage());
            ctx.redirect("/403");
        }
    }
}
