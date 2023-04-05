package Controller;

import Service.Baloot;

import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/credit")
public class CreditController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Baloot.getInstance().isUserLoggedIn())
            request.getRequestDispatcher("credit.jsp").forward(request, response);
        else response.sendRedirect("/login");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        float amount = Float.parseFloat(request.getParameter("credit"));
        try {
            Baloot.getInstance().addUserCredit(Baloot.getInstance().getLoggedInUser().getUsername(), amount);
            request.setAttribute("credit", amount);
            response.sendRedirect("/");
        } catch (Exception e){
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response); //todo: baraye error controller joda mikhad?
        }
    }
}