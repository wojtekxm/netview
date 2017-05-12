package zesp03.webapp.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.webapp.dto.UserDto;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PermissionFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(PermissionFilter.class);

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        //TODO odkomentować jak u Pociechy będzie działać apka mobilna
        final Boolean isStaticResource = (Boolean) req.getAttribute(StaticResourceFilter.ATTR_IS_STATIC);
        if (isStaticResource) {
            chain.doFilter(req, resp);
            return;
        }
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            final HttpServletRequest hreq = (HttpServletRequest) req;
            final HttpServletResponse hresp = (HttpServletResponse) resp;
            final UserDto loggedUser = (UserDto)hreq.getAttribute(AuthenticationFilter.ATTR_USERDTO);
            final String uri = hreq.getRequestURI();
            if( uri.equals("/") ||
                    uri.startsWith("/activate-account") ||
                    uri.startsWith("/login") ||
                    uri.startsWith("/api/login") ) {
                chain.doFilter(req, resp);
                return;
            }
            if(loggedUser == null) {
                if(uri.startsWith("/api/")) {
                    hresp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                else {
                    hresp.sendRedirect("/login");
                    return;
                }
            }
            else {
                //TODO czy jest rootem...
                chain.doFilter(req, resp);
                return;
            }
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
