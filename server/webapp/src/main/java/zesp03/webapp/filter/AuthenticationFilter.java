package zesp03.webapp.filter;

import zesp03.webapp.config.Cookies;
import zesp03.webapp.dto.UserDto;
import zesp03.webapp.service.LoginService;
import zesp03.webapp.service.LoginServiceImpl;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
    public static final String COOKIE_USERID = "userid";
    public static final String COOKIE_PASSTOKEN = "passtoken";
    // mapuje do UserDto, null jeśli uwierzytelnianie się nie powiodło
    public static final String ATTR_USERDTO = "loggedUser";

    private final LoginService loginService;

    public AuthenticationFilter() {
        //TODO ! spring ?
        this.loginService = new LoginServiceImpl();
    }

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
            Cookie cookieUid = Cookies.find(hreq, COOKIE_USERID);
            Cookie cookiePass = Cookies.find(hreq, COOKIE_PASSTOKEN);
            if (cookieUid != null && cookiePass != null) {
                Long userId = null;
                if (cookieUid.getValue() != null) {
                    try {
                        userId = Long.parseLong(cookieUid.getValue());
                    } catch (NumberFormatException ignore) {
                    }
                }
                final String passToken = cookiePass.getValue();
                UserDto userDto = null;

                if (userId != null && passToken != null) {
                    userDto = loginService.authenticate(userId, passToken);
                }

                if (userDto != null) {
                    cookieUid.setMaxAge(60 * 60 * 24 * 30);
                    hresp.addCookie(cookieUid);
                    cookiePass.setMaxAge(60 * 60 * 24 * 30);
                    hresp.addCookie(cookiePass);
                    hreq.setAttribute(ATTR_USERDTO, userDto);
                } else {
                    cookieUid.setValue("");
                    cookieUid.setMaxAge(0);
                    hresp.addCookie(cookieUid);
                    cookiePass.setValue("");
                    cookiePass.setMaxAge(0);
                    hresp.addCookie(cookiePass);
                }
            }
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
