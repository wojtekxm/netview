package zesp03.webapp.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class AllowPublicFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AllowLoggedFilter.class);

    private static final String ATTR_MARKED = "zesp03.webapp.filter.AllowPublicFilter.ATTR_MARKED";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        req.setAttribute(ATTR_MARKED, true);
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    public static boolean marked(ServletRequest req) {
        return req.getAttribute(ATTR_MARKED) != null;
    }
}
