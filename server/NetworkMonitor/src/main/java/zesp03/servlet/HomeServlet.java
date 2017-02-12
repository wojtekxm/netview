package zesp03.servlet;

import zesp03.data.row.UserRow;
import zesp03.filter.AuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "", name = "HomeServlet")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserRow userRow = (UserRow) request.getAttribute(AuthenticationFilter.ATTR_USERDATA);
        if (userRow != null) {
            if (userRow.isAdmin()) {
                request.getRequestDispatcher("/WEB-INF/view/Home_Admin.jsp").include(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/view/Home_User.jsp").include(request, response);
            }
        } else {
            request.getRequestDispatcher("/WEB-INF/view/Home_NonUser.jsp").include(request, response);
        }
    }
}
