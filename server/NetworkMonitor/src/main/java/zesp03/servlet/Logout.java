package zesp03.servlet;

import zesp03.filter.AuthFilter;
import zesp03.util.Cookies;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Logout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        Cookie cu = Cookies.find(request, AuthFilter.COOKIE_USERID);
        if (cu != null) {
            cu.setValue("");
            cu.setMaxAge(0);
            response.addCookie(cu);
        }
        Cookie cp = Cookies.find(request, AuthFilter.COOKIE_PASSTOKEN);
        if (cp != null) {
            cp.setValue("");
            cp.setMaxAge(0);
            response.addCookie(cp);
        }
        response.sendRedirect("/index.jsp");
    }
}