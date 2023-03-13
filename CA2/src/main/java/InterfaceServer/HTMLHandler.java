package InterfaceServer;

import Baloot.Commodity;
import Database.Database;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.File;

class CommoditiesListHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        Document doc = Jsoup.parse(new File("CA2/src/main/resources/Templates/Commodities.html"), "UTF-8");
        Element table = doc.select("table").first();
        for(Commodity commodity : Database.getInstance().getCommodities()){ //todo: make a func for commodity
            Element row = new Element("tr");
            row.appendElement("td").text(commodity.getId().toString());
            row.appendElement("td").text(commodity.getName().toString());
            row.appendElement("td").text(commodity.getProviderId().toString());
            row.appendElement("td").text(commodity.getPrice().toString());
            row.appendElement("td").text(commodity.getCategories().toString());
            row.appendElement("td").text(commodity.getRating().toString());
            row.appendElement("td").text(String.valueOf(commodity.getInStock()));
            Element link = new Element("a").attr("href", "/commodities/" + commodity.getId()).text("Link");
            row.appendElement("td").appendChild(link);
            table.appendChild(row);
        }
        ctx.contentType("text/html");
        ctx.result(doc.toString());
    }
}