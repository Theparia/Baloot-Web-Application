package HTMLHandler;

import Baloot.*;
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
    public void handle(@NotNull Context ctx) throws IOException {
        Document doc = Jsoup.parse(new File("CA2/src/main/resources/Templates/Commodity.html"), "UTF-8");
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

    private void constructCommentsTable(Element doc, String commodityId){ //todo shorten this
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

    private Element generateVoteButton(String type, int commentId, int count) {
        return new Element("td")
                .append("<label>").text(String.valueOf(count))
                .append("<input type=\"hidden\" name=\"commentId\" value=\"" + commentId + "\">")
                .append("<button type=\"submit\" formaction=\"/voteComment/" + (type.equals("like") ? "1" : "-1") + "\">")
                .text(type);
    }
}
