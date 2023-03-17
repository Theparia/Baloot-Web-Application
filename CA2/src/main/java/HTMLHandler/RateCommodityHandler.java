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
        try{
            String method = String.valueOf(ctx.method());
            Integer commodityId = Integer.valueOf(ctx.pathParam("commodityId"));
            String userId = "";
            Integer rate = 0;
            if (method.equals("GET")) {
                userId = ctx.pathParam("username");
                rate = Integer.valueOf(ctx.pathParam("rate"));
            } else if (method.equals("POST")) {
                userId = ctx.formParam("userId");
                rate = Integer.valueOf(ctx.formParam("quantity"));
            }
            baloot.rateCommodity(userId, commodityId, rate);
            ctx.redirect("/200");
        } catch (Exception e){
            ctx.redirect("/403");
        }
    }
}
