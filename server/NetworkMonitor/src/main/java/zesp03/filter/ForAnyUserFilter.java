package zesp03.filter;

import zesp03.data.row.UserRow;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ForAnyUserFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        UserRow userRow = (UserRow) req.getAttribute(AuthenticationFilter.ATTR_USERDATA);
        if (userRow != null) {
            chain.doFilter(req, resp);
            return;
        }
        if (resp instanceof HttpServletResponse) {
            final HttpServletResponse hresp = (HttpServletResponse) resp;
            hresp.sendError(HttpServletResponse.SC_FORBIDDEN, "you need to be logged in");
            return;
        }
        resp.flushBuffer();
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
