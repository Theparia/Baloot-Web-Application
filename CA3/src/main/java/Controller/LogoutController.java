package Controller;

import Service.Baloot;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            Baloot.getInstance().logout();
            request.getSession().invalidate();
            response.sendRedirect("/login");
        }catch (Exception e){
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("View/error.jsp").forward(request, response);
        }
    }
}
