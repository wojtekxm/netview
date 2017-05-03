package zesp03.webapp.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.common.core.App;

import javax.servlet.*;
import java.io.IOException;

public class ApiDelayFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(ApiDelayFilter.class);

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        int sd = App.getServerDelay();
        try {
            if(sd > 0) {
                Thread.sleep(sd);
            }
        }
        catch(InterruptedException exc) {
            log.warn("sleep interrupted: {}", exc.getLocalizedMessage());
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}