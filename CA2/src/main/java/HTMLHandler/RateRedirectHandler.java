package HTMLHandler;

import Baloot.Baloot;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class RateRedirectHandler implements Handler {
    private Baloot baloot;
    public RateRedirectHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) {
        try {
            String userId = ctx.formParam("userId");
            String rate = ctx.formParam("quantity");
            String commodityId = ctx.pathParam("commodityId");
            ctx.redirect("/rateCommodity/" + userId + "/" + commodityId + "/" + rate);
        }
        catch (Exception e){
            ctx.redirect("/403");
        }
    }
}
