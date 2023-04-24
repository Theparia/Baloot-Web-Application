package Controller;

import Service.Baloot;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/buyList")
public class BuyListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Baloot.getInstance().isUserLoggedIn())
            request.getRequestDispatcher("View/buyList.jsp").forward(request, response);
        else response.sendRedirect("/login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "removeCommodityFromBuyList":
                String commodityId = request.getParameter("removeCommodityId");
                removeCommodityFromBuyList(request, response, commodityId);
                break;
            case "applyDiscountCode":
                String discountCode = request.getParameter("discountCode");
                applyDiscount(request, response, discountCode);
                break;
            case "payment":
                pay(request, response);
        }
    }

    private void removeCommodityFromBuyList(HttpServletRequest request, HttpServletResponse response, String commodityId) throws ServletException, IOException {
        try {
            Baloot.getInstance().removeFromBuyList(Baloot.getInstance().getLoggedInUser().getUsername(), Integer.parseInt(commodityId));
            response.sendRedirect("/buyList");
        } catch (Exception e){
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("View/error.jsp").forward(request, response);
        }
    }

    private void applyDiscount(HttpServletRequest request, HttpServletResponse response, String discountCode) throws ServletException, IOException {
        try {
            Baloot.getInstance().applyDiscountCode(Baloot.getInstance().getLoggedInUser().getUsername(), discountCode);
            response.sendRedirect("/buyList");
        } catch (Exception e){
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("View/error.jsp").forward(request, response);
        }
    }

    private void pay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Baloot.getInstance().finalizePayment(Baloot.getInstance().getLoggedInUser().getUsername());
            response.sendRedirect("/buyList");
        } catch (Exception e){
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("View/error.jsp").forward(request, response);
        }
    }
}