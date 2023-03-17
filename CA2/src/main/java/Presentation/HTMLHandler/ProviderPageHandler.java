package Presentation.HTMLHandler;

import Domain.Commodity;
import Domain.Provider;
import Service.Baloot;
import com.google.common.io.Resources;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.List;

public class ProviderPageHandler implements Handler {
    private Baloot baloot;
    public ProviderPageHandler(Baloot baloot){
        this.baloot = baloot;
    }
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        try {
            Document doc = Jsoup.parse(new File(Resources.getResource("Templates/Provider.html").toURI()), "UTF-8");
            String providerId = ctx.pathParam("provider_id");
            Provider provider = baloot.findProviderById(Integer.valueOf(providerId));
            List<Commodity> providedCommodities = baloot.findCommoditiesByProvider(Integer.valueOf(providerId));
            doc.getElementById("id").text("Id: " + providerId);
            doc.getElementById("name").text("Name: " + provider.getName());
            doc.getElementById("registryDate").text("Registry Date: " + provider.getRegistryDate().toString());
            doc.getElementById("averageRating").text("Average Rating of Provided Commodities: " + baloot.getAverageRating(providerId));

            Element table = doc.selectFirst("table");
            for(Commodity commodity : providedCommodities){
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
