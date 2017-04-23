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
    public static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    public static final String COOKIE_USERID = "userid";
    public static final String COOKIE_PASSTOKEN = "passtoken";
    // mapuje do UserDto, null jeśli uwierzytelnianie się nie powiodło
    public static final String ATTR_USERDTO = "loggedUser";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        Boolean isStaticResource = (Boolean) req.getAttribute(StaticResourceFilter.ATTR_IS_STATIC);
        if (isStaticResource) {
            chain.doFilter(req, resp);
            return;
        }
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            final HttpServletRequest hreq = (HttpServletRequest) req;
            final HttpServletResponse hresp = (HttpServletResponse) resp;
            UserDto user = null;
            Cookie cookieUid = Cookies.find(hreq, COOKIE_USERID);
            Cookie cookiePass = Cookies.find(hreq, COOKIE_PASSTOKEN);
            if (cookieUid != null && cookieUid.getValue() != null
                    && cookiePass != null && cookiePass.getValue() != null) {
                try {
                    long id = Long.parseLong(cookieUid.getValue());
                    user = UglyUserServiceHolder.getLoginService().authenticate(id, cookiePass.getValue());
                } catch (NumberFormatException ignore) {}
            }
            hreq.setAttribute(ATTR_USERDTO, user);
            chain.doFilter(req, resp);
            return;
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
