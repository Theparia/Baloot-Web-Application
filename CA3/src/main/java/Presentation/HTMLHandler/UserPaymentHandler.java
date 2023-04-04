package Presentation.HTMLHandler;

import Service.Baloot;
import Service.Exceptions.CommodityOutOfStock;
import Service.Exceptions.NotEnoughCredit;
import Service.Exceptions.UserNotFound;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class UserPaymentHandler implements Handler {
    private Baloot baloot;
    public UserPaymentHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) {
        try {
            String userId = ctx.formParam("form_payment_userId");
            baloot.finalizePayment(userId);
            ctx.redirect("/users/" + userId);
        } catch (Exception e) {
            ctx.redirect("/403");
        }
    }
}
