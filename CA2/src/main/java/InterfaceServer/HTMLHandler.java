package InterfaceServer;

import Baloot.Baloot;
import Baloot.Commodity;
import Baloot.Provider;
import Database.Database;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.File;

import static java.lang.Float.parseFloat;

class CommoditiesListHandler implements Handler {
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


class CommoditytPageHandler implements Handler {
    private Baloot baloot;
    public CommoditytPageHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        try {
            Document doc = Jsoup.parse(new File("CA2/src/main/resources/Templates/Commodity.html"), "UTF-8");
            String commodityId = ctx.pathParam("commodity_id");
            Commodity commodity = baloot.findCommodityById(Integer.valueOf(commodityId));

            ctx.contentType("text/html");
            ctx.result(doc.toString());
        } catch (Exception e){
            ctx.redirect("/404");
        }
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
            ctx.redirect("/404");
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
        row.append("<td><a href=\"commodities/" + commodity.getId() +"\">Link</a></td>");
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
            System.out.println(e.getMessage());
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
