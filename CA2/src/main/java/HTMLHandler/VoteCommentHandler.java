package HTMLHandler;

import Baloot.Baloot;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class VoteCommentHandler implements Handler {
    Baloot baloot;
    public VoteCommentHandler(Baloot baloot){
        this.baloot = baloot;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        try{
            String username = ctx.pathParam("username");
            Integer commentId = Integer.valueOf(ctx.pathParam("commentId"));
            int vote = Integer.valueOf(ctx.pathParam("vote"));
            baloot.voteComment(commentId, username, vote);
            ctx.redirect("/200");
        } catch (Exception e){
            ctx.redirect("/403");
        }
    }
}