package HTMLHandler;

import Baloot.Baloot;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class VoteRedirectHandler implements Handler {
    private Baloot baloot;
    public VoteRedirectHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) {
        try {
            String userId = ctx.formParam("userId");
            String commentId = ctx.formParam("commentId");
            String vote = ctx.pathParam("vote");
            ctx.redirect("/voteComment/" + userId + "/" + commentId + "/" + vote);
        }
        catch (Exception e){
            ctx.redirect("/403");
        }
    }
}