package zesp03.filter;

import zesp03.data.UserData;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ForAdminFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        UserData userData = (UserData) req.getAttribute(AuthenticationFilter.ATTR_USERDATA);
        if (userData != null && userData.isAdmin()) {
            chain.doFilter(req, resp);
            return;
        }
        if (resp instanceof HttpServletResponse) {
            final HttpServletResponse hresp = (HttpServletResponse) resp;
            hresp.sendError(HttpServletResponse.SC_FORBIDDEN, "you need to be logged in as admin");
            return;
        }
        resp.flushBuffer();
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
