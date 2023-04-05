package Controller;

import Service.Baloot;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/commodities/*")
public class CommodityController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!Baloot.getInstance().isUserLoggedIn()) {
            response.sendRedirect("/login");
        }
        else {
            try{
                int commodityId = Integer.parseInt(request.getPathInfo().substring(1));
                request.setAttribute("commodity", Baloot.getInstance().findCommodityById(commodityId));
                request.getServletContext().getRequestDispatcher("/commodity.jsp").forward(request, response);
            } catch (Exception e){
                request.setAttribute("errorMessage", e.getMessage());
                request.getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);//todo: baraye error controller joda mikhad?
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        int commodityId = Integer.parseInt(request.getPathInfo().substring(1));
        String action = request.getParameter("action");
        switch (action){
            case "addComment":
                break;
            case "addRating":
                break;
            case "addToBuyList":
                break;
            case "likeComment":
                break;
            case "dislikeComment":
                break;
        }
    }
}
