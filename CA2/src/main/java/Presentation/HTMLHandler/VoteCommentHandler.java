package Presentation.HTMLHandler;

import Service.Baloot;
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
            String method = String.valueOf(ctx.method());
            int vote = Integer.valueOf(ctx.pathParam("vote"));
            String userId = "";
            Integer commentId = 0;
            if (method.equals("GET")) {
                userId = ctx.pathParam("username");
                commentId = Integer.valueOf(ctx.pathParam("commentId"));
            } else if (method.equals("POST")) {
                userId = ctx.formParam("userId");
                commentId = Integer.valueOf(ctx.formParam("commentId"));
            }
            baloot.voteComment(commentId, userId, vote);
            ctx.redirect("/200");
        } catch (Exception e){
            ctx.redirect("/403");
        }
    }
}