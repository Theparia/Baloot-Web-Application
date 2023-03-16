package InterfaceServer;

import Baloot.*;
import Baloot.Commodity;
import Baloot.Comment;
import Baloot.Exceptions.CommodityOutOfStock;
import Baloot.Exceptions.NotEnoughCredit;
import Baloot.Exceptions.UserNotFound;
import Database.Database;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;



import static java.lang.Float.parseFloat;

class CommoditiesListHandler implements Handler {

    private Baloot baloot;
    public CommoditiesListHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        Document doc = Jsoup.parse(new File("CA2/src/main/resources/Templates/Commodities.html"), "UTF-8");
        Element table = doc.selectFirst("table");
        for(Commodity commodity : Database.getInstance().getCommodities())
            table.appendChild(getHtmlTableRow(commodity));
        ctx.contentType("text/html");
        ctx.result(doc.toString());
    }

    private Element getHtmlTableRow(Commodity commodity){
        Element row = new Element("tr");
        row.append("<td>" + commodity.getId() + "</td>");
        row.append("<td>" + commodity.getName() + "</td>");
        row.append("<td>" + commodity.getProviderId() + "</td>");
        row.append("<td>" + commodity.getPrice() + "</td>");
        row.append("<td>" + commodity.getCategories() + "</td>");
        row.append("<td>" + commodity.getRating() + "</td>");
        row.append("<td>" + commodity.getInStock() + "</td>");
        row.append("<td><a href=\"commodities/" + commodity.getId() +"\">Link</a></td>");
        return row;
    }
}

class UserPageHandler implements Handler{
    private Baloot baloot;
    public UserPageHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        Document doc = Jsoup.parse(new File("CA2/src/main/resources/Templates/User.html"), "UTF-8");
        String userId = ctx.pathParam("user_id");
        try {
            User user = baloot.findUserByUsername(userId);
            doc.getElementById("username").text("Username: " + user.getUsername());
            doc.getElementById("email").text("Email: " + user.getEmail());
            doc.getElementById("birthDate").text("Birth Date: " + user.getBirthDate());
            doc.getElementById("address").text(user.getBirthDate());
            doc.getElementById("credit").text("Credit: " + String.valueOf(user.getCredit()));
            Element paymentForm = doc.select("form").first();
            paymentForm.attr("action", "/payment");

            doc.getElementById("form_payment_userId").attr("value", userId);

            Element buyListTable = doc.select("table").first();
            List<Commodity> buyList = baloot.findUserByUsername(userId).getBuyList();
            constructHTMLCommodityListTable(buyListTable, buyList, userId, true);

            Element purchasedListTable = doc.getElementById("purchasedList");
            List<Commodity> purchasedList = baloot.findUserByUsername(userId).getPurchasedList();
            constructHTMLCommodityListTable(purchasedListTable, purchasedList, userId, false);

        } catch(Exception e){
            ctx.redirect("/403");
        }
        ctx.contentType("text/html");
        ctx.result(doc.toString());
    }

    private void constructHTMLCommodityListTable(Element table, List<Commodity> commodities, String userId, boolean removeButton) {
        for (Commodity commodity : commodities) {
            Element row = getHtmlTableRow(commodity);
            if (!removeButton){
                table.appendChild(row);
            }
            else{
                Element form = constructHTMLForm(commodity, row);
                form.attr("action", "/removeFromBuyList/" + userId + "/" + commodity.getId());
                Element button = new Element("button");
                Element formCell = new Element("td");
                button.attr("type", "submit");
                button.text("Remove");
                form.appendChild(button);
                formCell.appendChild(form);
                row.appendChild(formCell);
                table.appendChild(row);
            }
        }
    }

    private Element constructHTMLForm(Commodity commodity, Element row){
        Element linkCell = new Element("td");
        Element link = new Element("a");
        link.attr("href", "/commodities/" + commodity.getId());
        link.text("Link");
        linkCell.appendChild(link);
        row.appendChild(linkCell);
        Element form = new Element("form");
        form.attr("action", "");
        form.attr("method", "POST");
        return form;
    }

    private Element getHtmlTableRow(Commodity commodity){
        Element row = new Element("tr");
        row.append("<th>" + commodity.getId() + "</th>");
        row.append("<th>" + commodity.getName() + "</th>");
        row.append("<th>" + commodity.getProviderId() + "</th>");
        row.append("<th>" + commodity.getPrice() + "</th>");
        row.append("<th>" + commodity.getCategories() + "</th>");
        row.append("<th>" + commodity.getRating() + "</th>");
        row.append("<th>" + commodity.getInStock() + "</th>");
        return row;
    }
}

class UserPaymentHandler implements Handler{
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
        } catch (UserNotFound | CommodityOutOfStock | NotEnoughCredit exception) {
            ctx.redirect("/403");
        }
    }
}


class CommodityPageHandler implements Handler{
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

    public void constructCommentsTable(Element doc, String commodityId){
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



class voteRedirectHandler implements Handler{
    private Baloot baloot;
    public voteRedirectHandler(Baloot baloot){
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

class addToBuyListRedirectHandler implements Handler{
    private Baloot baloot;
    public addToBuyListRedirectHandler(Baloot baloot){
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

class rateRedirectHandler implements Handler{
    private Baloot baloot;
    public rateRedirectHandler(Baloot baloot){
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

class addToBuyListHandler implements Handler{
    private Baloot baloot;
    public addToBuyListHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) {
        try {
            String commodityId = ctx.pathParam("commodityId");
            String userId = ctx.pathParam("username");
            baloot.addToBuyList(userId, Integer.valueOf(commodityId));
            ctx.redirect("/200");
        }catch (Exception e){
            ctx.redirect("/403");
        }
        ctx.contentType("text/html");
    }
}

class removeFromBuyListHandler implements Handler{
    private Baloot baloot;
    public removeFromBuyListHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) {
        try {
            String commodityId = ctx.pathParam("commodityId");
            String username = ctx.pathParam("username");
            baloot.removeFromBuyList(username, Integer.valueOf(commodityId));
            ctx.redirect("/200");
        }catch (Exception e){
            ctx.redirect("/403");
        }
        ctx.contentType("text/html");
    }
}

class ProviderPageHandler implements Handler {
    private Baloot baloot;
    public ProviderPageHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        try {
            Document doc = Jsoup.parse(new File("CA2/src/main/resources/Templates/Provider.html"), "UTF-8");
            String providerId = ctx.pathParam("provider_id");
            Provider provider = baloot.findProviderById(Integer.valueOf(providerId));
            doc.getElementById("id").text("Id: " + providerId);
            doc.getElementById("name").text("Name: " + provider.getName());
            doc.getElementById("registryDate").text("Registry Date: " + provider.getRegistryDate().toString());
            Element table = doc.selectFirst("table");
            for(Commodity commodity : baloot.findCommoditiesByProvider(Integer.valueOf(providerId))){
                table.appendChild(getHtmlTableRow(commodity));
            }
            ctx.contentType("text/html");
            ctx.result(doc.toString());
        } catch (Exception e){
            ctx.redirect("/403");
        }
    }

    private Element getHtmlTableRow(Commodity commodity){
        Element row = new Element("tr");
        row.append("<td>" + commodity.getId() + "</td>");
        row.append("<td>" + commodity.getName() + "</td>");
        row.append("<td>" + commodity.getPrice() + "</td>");
        row.append("<td>" + commodity.getCategories() + "</td>");
        row.append("<td>" + commodity.getRating() + "</td>");
        row.append("<td>" + commodity.getInStock() + "</td>");
        row.append("<td><a href=\"/commodities/" + commodity.getId() +"\">Link</a></td>");
        return row;
    }
}

class AddCreditHandler implements Handler {
    Baloot baloot;
    public AddCreditHandler(Baloot baloot){
        this.baloot = baloot;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        try{
            String userId = ctx.pathParam("user_id");
            float credit = parseFloat(ctx.pathParam("credit"));
            baloot.addUserCredit(userId, credit);
            ctx.redirect("/200");
        } catch (Exception e){
            ctx.redirect("/403");
        }
    }
}

class RateCommodityHandler implements Handler{
    Baloot baloot;
    public RateCommodityHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        try {
            String username = ctx.pathParam("username");
            Integer commodityId = Integer.valueOf(ctx.pathParam("commodityId"));
            Integer rate = Integer.valueOf(ctx.pathParam("rate"));
            baloot.rateCommodity(username, commodityId, rate);
            ctx.redirect("/200");
        } catch (Exception e){
            System.out.println(e.getMessage());
            ctx.redirect("/403");
        }
    }
}

class VoteCommentHandler implements Handler {
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


class SearchCommoditiesByPriceHandler implements Handler{
    Baloot baloot;
    public SearchCommoditiesByPriceHandler(Baloot baloot){
        this.baloot = baloot;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        try{
            Document doc = Jsoup.parse(new File("CA2/src/main/resources/Templates/Commodities.html"), "UTF-8");
            float startPrice = parseFloat(ctx.pathParam("start_price"));
            float endPrice = parseFloat(ctx.pathParam("end_price"));
            Element table = doc.selectFirst("table");
            for(Commodity commodity : baloot.searchCommoditiesByPrice(startPrice, endPrice))
                table.appendChild(getHtmlTableRow(commodity));
            ctx.contentType("text/html");
            ctx.result(doc.toString());
        } catch (Exception e){
            ctx.redirect("/403");
        }
    }

    private Element getHtmlTableRow(Commodity commodity){
        Element row = new Element("tr");
        row.append("<td>" + commodity.getId() + "</td>");
        row.append("<td>" + commodity.getName() + "</td>");
        row.append("<td>" + commodity.getProviderId() + "</td>");
        row.append("<td>" + commodity.getPrice() + "</td>");
        row.append("<td>" + commodity.getCategories() + "</td>");
        row.append("<td>" + commodity.getRating() + "</td>");
        row.append("<td>" + commodity.getInStock() + "</td>");
        row.append("<td><a href=\"commodities/" + commodity.getId() +"\">Link</a></td>");
        return row;
    }
}


class SearchCommoditiesByCategoryHandler implements Handler{
    Baloot baloot;
    public SearchCommoditiesByCategoryHandler(Baloot baloot){
        this.baloot = baloot;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        try{
            Document doc = Jsoup.parse(new File("CA2/src/main/resources/Templates/Commodities.html"), "UTF-8");
            String category = ctx.pathParam("categories");
            Element table = doc.selectFirst("table");
            for(Commodity commodity : baloot.findCommoditiesByCategory(category))
                table.appendChild(getHtmlTableRow(commodity));
            ctx.contentType("text/html");
            ctx.result(doc.toString());
        } catch (Exception e){
            ctx.redirect("/403");
        }
    }

    private Element getHtmlTableRow(Commodity commodity){
        Element row = new Element("tr");
        row.append("<td>" + commodity.getId() + "</td>");
        row.append("<td>" + commodity.getName() + "</td>");
        row.append("<td>" + commodity.getProviderId() + "</td>");
        row.append("<td>" + commodity.getPrice() + "</td>");
        row.append("<td>" + commodity.getCategories() + "</td>");
        row.append("<td>" + commodity.getRating() + "</td>");
        row.append("<td>" + commodity.getInStock() + "</td>");
        row.append("<td><a href=\"commodities/" + commodity.getId() +"\">Link</a></td>");
        return row;
    }
}

class StatusCodePageHandler implements Handler {
    private String code;
    public StatusCodePageHandler(String code){
        this.code = code;
    }
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        Document doc = Jsoup.parse(new File("CA2/src/main/resources/Templates/" + code + ".html"), "UTF-8");
        ctx.contentType("text/html");
        ctx.result(doc.toString());
    }
}


