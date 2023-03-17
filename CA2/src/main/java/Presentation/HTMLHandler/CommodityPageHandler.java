package Presentation.HTMLHandler;

import Service.Exceptions.UserNotFound;
import Domain.Comment;
import Domain.Commodity;
import Service.Baloot;
import com.google.common.io.Resources;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CommodityPageHandler implements Handler {
    private Baloot baloot;
    public CommodityPageHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        Document doc = Jsoup.parse(new File(Resources.getResource("Templates/Commodity.html").toURI()), "UTF-8");
        String commodityId = ctx.pathParam("commodity_id");
        try {
            Commodity commodity = baloot.findCommodityById(Integer.valueOf(commodityId));
            doc.getElementById("id").text("Id: " + commodity.getId());
            doc.getElementById("name").text("Name: " + commodity.getName());
            doc.getElementById("providerId").text("Provider Id: " + commodity.getProviderId());
            doc.getElementById("price").text("Price: " + commodity.getPrice());
            doc.getElementById("categories").text("Categories: " + commodity.getCategories());
            doc.getElementById("rating").text("Rating: " + commodity.getRating());
            doc.getElementById("inStock").text("In Stock: " + commodity.getInStock());

            Element rateCommodityButton = doc.getElementById("rate");
            rateCommodityButton.attr("formaction", "/rateCommodity/" + commodityId);

            Element addToBuyListButton = doc.getElementById("add_to_buyList");
            addToBuyListButton.attr("formaction", "/addToBuyList/" + commodityId);

            constructCommentsTable(doc, commodityId);

        } catch(Exception e){
            ctx.redirect("/403");
        }
        ctx.contentType("text/html");
        ctx.result(doc.toString());

    }

    private void constructCommentsTable(Element doc, String commodityId) throws UserNotFound {
        List<Comment> comments = baloot.getCommodityComments(commodityId);
        for (Comment comment : comments) {
            Element commentRow = doc.getElementById("commentsTable").appendElement("tr");
            String username = baloot.getUsernameByEmail(comment.getUserEmail());
            commentRow.appendElement("td").text(username);
            commentRow.appendElement("td").text(comment.getText());
            commentRow.appendElement("td").text(comment.getDate());

            Element likeButton = commentRow.appendElement("td");
            likeButton.appendElement("label").text(String.valueOf(comment.getLikeCount()));
            likeButton.appendElement("input").attr("type", "hidden").attr("name", "commentId").attr("value", String.valueOf(comment.getId()));
            likeButton.appendElement("button").attr("type", "submit").attr("formaction", "/voteComment/1").text("like");

            Element dislikeButton = commentRow.appendElement("td");
            dislikeButton.appendElement("label").text(String.valueOf(comment.getDisLikeCount()));
            dislikeButton.appendElement("input").attr("type", "hidden").attr("name", "commentId").attr("value", String.valueOf(comment.getId()));
            dislikeButton.appendElement("button").attr("type", "submit").attr("formaction", "/voteComment/-1").text("dislike");
        }
    }
}
