package Presentation.HTMLHandler;

import Domain.Commodity;
import Domain.User;
import Service.Baloot;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.List;

public class UserPageHandler implements Handler {
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
            doc.getElementById("address").text("Address: " + user.getAddress());
            doc.getElementById("credit").text("Credit: " + user.getCredit());
            Element paymentForm = doc.selectFirst("form");
            paymentForm.attr("action", "/payment");

            doc.getElementById("form_payment_userId").attr("value", userId);

            Element buyListTable = doc.selectFirst("table");
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
            if(removeButton){
                Element form = new Element("form")
                        .attr("method", "GET")
                        .attr("action", "/removeFromBuyList/" + userId + "/" + commodity.getId());

                Element button = new Element("button")
                        .attr("type", "submit")
                        .text("Remove");
                form.appendChild(button);
                row.appendChild(new Element("td").appendChild(form));
            }
            table.appendChild(row);
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
