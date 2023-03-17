package Presentation.HTMLHandler;

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

public class SearchCommoditiesByCategoryHandler implements Handler {
    Baloot baloot;
    public SearchCommoditiesByCategoryHandler(Baloot baloot){
        this.baloot = baloot;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        try{
            Document doc = Jsoup.parse(new File(Resources.getResource("Templates/Commodities.html").toURI()), "UTF-8");
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
        row.append("<td><a href=\"/commodities/" + commodity.getId() +"\">Link</a></td>");
        return row;
    }
}
