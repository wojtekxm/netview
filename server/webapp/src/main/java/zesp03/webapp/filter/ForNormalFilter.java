package zesp03.webapp.filter;

import zesp03.webapp.dto.UserDto;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ForNormalFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        UserDto user = (UserDto) req.getAttribute(AuthenticationFilter.ATTR_USERDTO);
        if (user != null) {
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
