package zesp03.filter;

import zesp03.util.Cookies;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {
    public static String COOKIE_USERID = "userid";
    public static String COOKIE_PASSTOKEN = "token";
    // mapuje do String
    public static String ATTR_USERNAME = "zesp03.filter.AuthFilter.ATTR_USERNAME";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            final HttpServletRequest hreq = (HttpServletRequest) req;
            final HttpServletResponse hresp = (HttpServletResponse) resp;
            Cookie userId = Cookies.find(hreq, COOKIE_USERID);
            Cookie passToken = Cookies.find(hreq, COOKIE_PASSTOKEN);
            if (userId != null && passToken != null) {
                if (userId.getValue().equals("1000") &&
                        passToken.getValue().equals("0123456789abcdef")) {
                    userId.setMaxAge(60 * 60 * 24 * 30);
                    passToken.setMaxAge(60 * 60 * 24 * 30);
                    hresp.addCookie(userId);
                    hresp.addCookie(passToken);
                    hreq.setAttribute(ATTR_USERNAME, "Marek");//?
                    chain.doFilter(req, resp);
                    return;
                } else {
                    userId.setValue("");
                    userId.setMaxAge(0);
                    passToken.setValue("");
                    passToken.setMaxAge(0);
                    hresp.addCookie(userId);
                    hresp.addCookie(passToken);
                }
            }
            hresp.sendRedirect("/login?error=1");
            return;
        }
        resp.flushBuffer();
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
