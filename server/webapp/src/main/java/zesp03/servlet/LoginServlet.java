package zesp03.servlet;

import zesp03.data.row.UserRow;
import zesp03.dto.LoginResultDto;
import zesp03.filter.AuthenticationFilter;
import zesp03.service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/login", name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    public static final String POST_USERNAME = "u";
    public static final String POST_PASSWORD = "p";
    public static final String GET_ERROR = "error";
    // mapuje do Boolean, opcjonalny
    public static final String ATTR_FAILED = "zesp03.servlet.LoginServlet.ATTR_FAILED";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter(POST_USERNAME);
        String password = request.getParameter(POST_PASSWORD);
        if (username != null && password != null) {
            LoginResultDto result = new LoginService().login(username, password);
            if (result.isSuccess()) {
                Cookie cu = new Cookie(
                        AuthenticationFilter.COOKIE_USERID,
                        Long.toString(result.getUserId())
                );
                cu.setMaxAge(60 * 60 * 24 * 30);
                cu.setPath("/");
                response.addCookie(cu);
                Cookie cp = new Cookie(
                        AuthenticationFilter.COOKIE_PASSTOKEN,
                        result.getPassToken()
                );
                cp.setMaxAge(60 * 60 * 24 * 30);
                cp.setPath("/");
                response.addCookie(cp);
                response.sendRedirect("/");//? home page
                return;
            } else {
                request.setAttribute(ATTR_FAILED, Boolean.TRUE);
                request.getRequestDispatcher("/WEB-INF/view/Login.jsp").include(request, response);
                return;
            }
        }
        request.getRequestDispatcher("/WEB-INF/view/Login.jsp").include(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserRow userRow = (UserRow) request.getAttribute(AuthenticationFilter.ATTR_USERROW);
        if (userRow != null) {
            response.sendRedirect("/");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/view/Login.jsp").include(request, response);
    }
}
