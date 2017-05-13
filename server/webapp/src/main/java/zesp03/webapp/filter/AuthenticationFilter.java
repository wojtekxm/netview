package zesp03.webapp.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.webapp.config.Cookies;
import zesp03.webapp.config.UglyUserServiceHolder;
import zesp03.webapp.dto.UserDto;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    public static final String COOKIE_USERID = "userid";
    public static final String COOKIE_PASSTOKEN = "passtoken";
    // mapuje do UserDto, null jeśli uwierzytelnianie się nie powiodło
    private static final String ATTR_USERDTO = "zesp03.webapp.filter.AuthenticationFilter.ATTR_USERDTO";
    private static final String ATTR_TID = "zesp03.webapp.filter.AuthenticationFilter.ATTR_TID";
    private static final String ATTR_TVAL = "zesp03.webapp.filter.AuthenticationFilter.ATTR_TVAL";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        if (StaticResourceFilter.marked(req)) {
            chain.doFilter(req, resp);
            return;
        }
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            final HttpServletRequest hreq = (HttpServletRequest) req;
            final HttpServletResponse hresp = (HttpServletResponse) resp;
            UserDto user = null;
            Cookie cookieTid = Cookies.find(hreq, COOKIE_USERID);
            Cookie cookieTval = Cookies.find(hreq, COOKIE_PASSTOKEN);
            Long tid = null;
            String tval = null;
            if (cookieTid != null && cookieTid.getValue() != null
                    && cookieTval != null && cookieTval.getValue() != null) {
                try {
                    tid = Long.parseLong(cookieTid.getValue());
                    tval = cookieTval.getValue();
                    user = UglyUserServiceHolder.getLoginService().authenticate(tid, cookieTval.getValue());
                } catch (NumberFormatException ignore) {}
            }
            hreq.setAttribute("loggedUser", user);
            hreq.setAttribute(ATTR_USERDTO, user);
            hreq.setAttribute(ATTR_TID, tid);
            hreq.setAttribute(ATTR_TVAL, tval);
            chain.doFilter(req, resp);
            return;
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    public static UserDto getUser(ServletRequest req) {
        return (UserDto)req.getAttribute(ATTR_USERDTO);
    }

    public static Long getTid(ServletRequest req) {
        return (Long)req.getAttribute(ATTR_TID);
    }

    public static String getTval(ServletRequest req) {
        return (String)req.getAttribute(ATTR_TVAL);
    }
}
