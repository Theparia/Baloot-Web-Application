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
                request.getServletContext().getRequestDispatcher("/View/commodity.jsp").forward(request, response);
            } catch (Exception e){
                request.setAttribute("errorMessage", e.getMessage());
                request.getServletContext().getRequestDispatcher("/View/error.jsp").forward(request, response);
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try {
            int commodityId = Integer.parseInt(request.getPathInfo().substring(1));
            String action = request.getParameter("action");
            switch (action) {
                case "addComment":
                    String text = request.getParameter("comment");
                    Baloot.getInstance().addComment(Baloot.getInstance().getLoggedInUser().getEmail(), commodityId, text);
                    break;
                case "addRating":
                    String rate = request.getParameter("rate");
                    Baloot.getInstance().rateCommodity(Baloot.getInstance().getLoggedInUser().getUsername(), commodityId, Integer.parseInt(rate));
                    break;
                case "addToBuyList":
                    Baloot.getInstance().addToBuyList(Baloot.getInstance().getLoggedInUser().getUsername(), commodityId);
                    break;
                case "likeComment":
                    int commentId = Integer.parseInt(request.getParameter("commentIdLike"));
                    Baloot.getInstance().voteComment(commentId, Baloot.getInstance().getLoggedInUser().getUsername(), 1);
                    break;
                case "dislikeComment":
                    commentId = Integer.parseInt(request.getParameter("commentIdDisLike"));
                    Baloot.getInstance().voteComment(commentId, Baloot.getInstance().getLoggedInUser().getUsername(), -1);
                    break;
            }
            response.sendRedirect("/commodities/" + commodityId);
        } catch (Exception e){
            request.setAttribute("errorMessage", e.getMessage());
            request.getServletContext().getRequestDispatcher("/View/error.jsp").forward(request, response);
        }
    }
}
