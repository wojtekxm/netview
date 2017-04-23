package zesp03.webapp.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StaticResourceFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(StaticResourceFilter.class);
    // mapuje do Boolean
    public static final String ATTR_IS_STATIC = "StaticResourceFilter.ATTR_IS_STATIC";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            final HttpServletRequest hreq = (HttpServletRequest) req;
            final HttpServletResponse hresp = (HttpServletResponse) resp;
            String r = hreq.getRequestURI();
            String[] prefixes = {"/images/", "/css/", "/fonts/", "/js/", "/favicon.ico"};
            boolean isStatic = false;
            for (String prefix : prefixes) {
                if (r.startsWith(prefix)) isStatic = true;
            }
            hreq.setAttribute(ATTR_IS_STATIC, isStatic);
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
