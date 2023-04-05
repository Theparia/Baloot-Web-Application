package Controller;

import Database.Database;
import Domain.Commodity;
import Service.Baloot;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/commodities")
public class CommoditiesController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Baloot.getInstance().isUserLoggedIn()) {
            HttpSession session = request.getSession();

            if(session.getAttribute("commodities") == null)
                session.setAttribute("commodities", Database.getInstance().getCommodities());

            request.getRequestDispatcher("commodities.jsp").forward(request, response);
        }
        else
            response.sendRedirect("/login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchedName = request.getParameter("search");
        HttpSession session = request.getSession();
        List<Commodity> commodities = (List<Commodity>)request.getSession().getAttribute("commodities");
        String action = request.getParameter("action");
        switch (action) {
            case "search_by_category":
                session.setAttribute("commodities", Baloot.getInstance().searchCommoditiesByCategory(searchedName, commodities));
                break;
            case "search_by_name":
                session.setAttribute("commodities", Baloot.getInstance().searchCommoditiesByName(searchedName, commodities));
                break;
            case "clear":
                session.removeAttribute("commodities");
                break;
            case "sort_by_price":
                session.setAttribute("commodities", Baloot.getInstance().sortCommoditiesByPrice(commodities));
                break;
        }
        response.sendRedirect("/commodities");
    }
}
