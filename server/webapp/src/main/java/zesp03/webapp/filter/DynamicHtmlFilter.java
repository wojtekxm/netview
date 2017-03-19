package zesp03.webapp.filter;

import javax.servlet.*;
import java.io.IOException;

public class DynamicHtmlFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        Boolean isStatic = (Boolean) req.getAttribute(StaticResourceFilter.ATTR_IS_STATIC);
        if (!isStatic) {
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
            resp.setContentType("text/html");
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
