package zesp03.filter;

import zesp03.core.Database;
import zesp03.core.Secret;
import zesp03.data.row.UserRow;
import zesp03.entity.User;
import zesp03.util.Cookies;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
    public static final String COOKIE_USERID = "userid";
    public static final String COOKIE_PASSTOKEN = "passtoken";
    // mapuje do UserRow, null jeśli uwierzytelnianie się nie powiodło
    public static final String ATTR_USERROW = "zesp03.filter.AuthenticationFilter.ATTR_USERROW";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        Boolean isStaticResource = (Boolean) req.getAttribute(StaticResourceFilter.ATTR_IS_STATIC);
        if (isStaticResource) {
            chain.doFilter(req, resp);
            return;
        }
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            final HttpServletRequest hreq = (HttpServletRequest) req;
            final HttpServletResponse hresp = (HttpServletResponse) resp;
            Cookie cookieUid = Cookies.find(hreq, COOKIE_USERID);
            Cookie cookiePass = Cookies.find(hreq, COOKIE_PASSTOKEN);
            if (cookieUid != null && cookiePass != null) {
                Long userId = null;
                if (cookieUid.getValue() != null) {
                    try {
                        userId = Long.parseLong(cookieUid.getValue());
                    } catch (NumberFormatException ignore) {
                    }
                }
                final String passToken = cookiePass.getValue();
                UserRow userRow = null;

                if (userId != null && passToken != null) {
                    EntityManager em = null;
                    EntityTransaction tran = null;
                    try {
                        em = Database.createEntityManager();
                        tran = em.getTransaction();
                        tran.begin();

                        User user = em.find(User.class, userId);
                        if (user != null)
                            userRow = new UserRow(user);

                        tran.commit();
                    } catch (RuntimeException exc) {
                        if (tran != null && tran.isActive()) tran.rollback();
                        throw exc;
                    } finally {
                        if (em != null) em.close();
                    }

                    if (userRow != null && userRow.getSecret() != null) {
                        if (!Secret.check(userRow.getSecret(), passToken))
                            userRow = null;
                    }
                }

                if (userRow != null) {
                    cookieUid.setMaxAge(60 * 60 * 24 * 30);
                    hresp.addCookie(cookieUid);
                    cookiePass.setMaxAge(60 * 60 * 24 * 30);
                    hresp.addCookie(cookiePass);
                    hreq.setAttribute(ATTR_USERROW, userRow);
                } else {
                    cookieUid.setValue("");
                    cookieUid.setMaxAge(0);
                    hresp.addCookie(cookieUid);
                    cookiePass.setValue("");
                    cookiePass.setMaxAge(0);
                    hresp.addCookie(cookiePass);
                }
            }
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
