package HTMLHandler;

import Baloot.Baloot;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class AddToBuyListRedirectHandler implements Handler {
    private Baloot baloot;
    public AddToBuyListRedirectHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) {
        try {
            String userId = ctx.formParam("userId");
            String commodityId = ctx.pathParam("commodityId");
            ctx.redirect("/addToBuyList/" + userId + "/" + commodityId);
        }
        catch (Exception e){
            ctx.redirect("/403");
        }
    }
}