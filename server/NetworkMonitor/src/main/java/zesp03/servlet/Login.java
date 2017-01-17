package zesp03.servlet;

import zesp03.filter.AuthFilter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Login extends HttpServlet {
    public static String POST_USERNAME = "u";
    public static String POST_PASSWORD = "p";
    public static String GET_ERROR = "error";
    // mapuje do Boolean, opcjonalny
    public static String ATTR_FAILED = "zesp03.servlet.Login.ATTR_FAILED";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        String username = request.getParameter(POST_USERNAME);
        String password = request.getParameter(POST_PASSWORD);
        if (username != null && password != null) {
            if (username.equals("mark") && password.equals("zuckerberg")) {
                Cookie cu = new Cookie(AuthFilter.COOKIE_USERID, "1000");
                cu.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(cu);
                Cookie cp = new Cookie(AuthFilter.COOKIE_PASSTOKEN, "0123456789abcdef");
                cu.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(cp);
                response.sendRedirect("/index.jsp");//? home page
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
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        request.getRequestDispatcher("/WEB-INF/view/Login.jsp").include(request, response);
    }
}
