package InterfaceServer;

import Baloot.*;
import Baloot.Commodity;
import Baloot.Exceptions.CommodityOutOfStock;
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
                Element input = constructHTMLInputTag(commodity);
                Element button = new Element("button");
                Element formCell = new Element("td");
                button.attr("type", "submit");
                button.text("Remove");
                form.appendChild(input);
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

    private Element constructHTMLInputTag(Commodity commodity){
        Element input = new Element("input");
        input.attr("type", "hidden");
        input.attr("name", "commodityId");
        input.attr("value", String.valueOf(commodity.getId()));
        return input;
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
            System.out.println("--> username: "+ userId);
            baloot.finalizePayment(userId);
            ctx.redirect("/users/" + userId);
        } catch (UserNotFound | CommodityOutOfStock exception) {
            ctx.redirect("/404");
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

            Element form = doc.getElementById("add_to_buyList");
            form.attr("formaction", "/addToBuyList/" + commodityId);

        } catch(Exception e){
            ctx.redirect("/403");
        }
        ctx.contentType("text/html");
        ctx.result(doc.toString());

    }
}

class addToBuyListRedirectHandler implements Handler{
    private Baloot baloot;
    public addToBuyListRedirectHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) {
        System.out.println("HERE");
        String commodityId = ctx.pathParam("commodityId");
        String userId = ctx.formParam("userId");
        System.out.println("---> /addToBuyList/" + userId + "/" + commodityId);
        ctx.redirect("/addToBuyList/" + userId + "/" + commodityId);
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
            System.out.println("HANDLER");
            String commodityId = ctx.pathParam("commodityId");
            String userId = ctx.pathParam("username");
            System.out.println("username: " + userId + " commodity: " + commodityId);
            baloot.addToBuyList(userId, Integer.valueOf(commodityId));
            ctx.redirect("/200");
        }catch (Exception e){
            ctx.redirect("/404");
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
            ctx.redirect("/404");
        }
        ctx.contentType("text/html");
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


