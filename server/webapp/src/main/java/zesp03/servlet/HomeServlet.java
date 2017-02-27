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
        UserRow userRow = (UserRow) request.getAttribute(AuthenticationFilter.ATTR_USERROW);
        if (userRow != null) {
            switch (userRow.getRole()) {
                case NORMAL:
                    request.getRequestDispatcher("/WEB-INF/view/Home_Regular.jsp").include(request, response);
                    break;
                case ADMIN:
                    request.getRequestDispatcher("/WEB-INF/view/Home_Admin.jsp").include(request, response);
                    break;
                case ROOT:
                    request.getRequestDispatcher("/WEB-INF/view/Home_Root.jsp").include(request, response);
                    break;
            }
        } else {
            request.getRequestDispatcher("/WEB-INF/view/Home_Stranger.jsp").include(request, response);
        }
    }
}
