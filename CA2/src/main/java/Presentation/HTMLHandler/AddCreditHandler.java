package Presentation.HTMLHandler;

import Service.Baloot;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import static java.lang.Float.parseFloat;

public class AddCreditHandler implements Handler {
    Baloot baloot;
    public AddCreditHandler(Baloot baloot){
        this.baloot = baloot;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        try{
            String method = String.valueOf(ctx.method());
            String userId = ctx.pathParam("user_id");
            float credit = 0;
            if (method.equals("GET")) {
                credit = parseFloat(ctx.pathParam("credit"));
            } else if (method.equals("POST")) {
                credit = parseFloat(ctx.formParam("creditAmount"));
            }
            baloot.addUserCredit(userId, credit);
            ctx.redirect("/200");
        } catch (Exception e){
            ctx.redirect("/403");
        }
    }
}

