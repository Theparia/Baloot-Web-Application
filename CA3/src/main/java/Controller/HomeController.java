package Controller;

import Service.Baloot;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("")
public class HomeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Baloot.getInstance().isUserLoggedIn())
            request.getRequestDispatcher("home.jsp").forward(request, response);
        else
            response.sendRedirect("/login");
    }
}
