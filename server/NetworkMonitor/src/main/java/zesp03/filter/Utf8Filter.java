package zesp03.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Utf8Filter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            final HttpServletRequest hreq = (HttpServletRequest) req;
            final HttpServletResponse hresp = (HttpServletResponse) resp;
            String r = hreq.getRequestURI();
            if (!(r.startsWith("/images") || r.startsWith("/favicon"))) {
                hreq.setCharacterEncoding("utf-8");
                hresp.setCharacterEncoding("utf-8");
            }
            chain.doFilter(req, resp);
        } else {
            resp.flushBuffer();
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
