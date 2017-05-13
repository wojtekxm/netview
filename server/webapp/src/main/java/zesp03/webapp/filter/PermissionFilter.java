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
        if (StaticResourceFilter.marked(req)) {
            chain.doFilter(req, resp);
            return;
        }
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            final HttpServletRequest hreq = (HttpServletRequest) req;
            final HttpServletResponse hresp = (HttpServletResponse) resp;
            final UserDto loggedUser = AuthenticationFilter.getUser(hreq);
            if(AllowPublicFilter.marked(req) || AllowLoggedFilter.marked(req)) {
                chain.doFilter(req, resp);
                return;
            }
            if(loggedUser != null) {
                if(loggedUser.isRoot()) {
                    chain.doFilter(req, resp);
                    return;
                }
                hresp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            final String uri = hreq.getRequestURI();
            if(uri.startsWith("/api/")) {
                //TODO zwrócić JSON?
                hresp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            hresp.sendRedirect("/login");
            return;
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
