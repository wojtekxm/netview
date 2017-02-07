package zesp03.filter;

import zesp03.data.UserData;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminOnlyFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            final HttpServletRequest hreq = (HttpServletRequest) req;
            final HttpServletResponse hresp = (HttpServletResponse) resp;
            UserData userData = (UserData) hreq.getAttribute(AuthenticationFilter.ATTR_USERDATA);
            if (userData != null && userData.isAdmin()) {
                chain.doFilter(hreq, hresp);
                return;
            } else {
                hresp.sendError(HttpServletResponse.SC_FORBIDDEN, "you need to be logged as admin");
                return;
            }
        }
        resp.flushBuffer();
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
