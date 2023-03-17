package HTMLHandler;

import Baloot.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;

import static java.lang.Float.parseFloat;

public class SearchCommoditiesByPriceHandler implements Handler {
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
        row.append("<td><a href=\"/commodities/" + commodity.getId() +"\">Link</a></td>");
        return row;
    }
}