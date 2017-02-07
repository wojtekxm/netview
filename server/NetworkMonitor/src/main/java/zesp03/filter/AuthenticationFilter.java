package zesp03.filter;

import zesp03.core.Database;
import zesp03.core.Secret;
import zesp03.data.UserData;
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
    // mapuje do UserData, null jeśli uwierzytelnianie się nie powiodło
    public static final String ATTR_USERDATA = "zesp03.filter.AuthenticationFilter.ATTR_USERDATA";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            final HttpServletRequest hreq = (HttpServletRequest) req;
            final HttpServletResponse hresp = (HttpServletResponse) resp;
            Cookie cookieUid = Cookies.find(hreq, COOKIE_USERID);
            Cookie cookiePass = Cookies.find(hreq, COOKIE_PASSTOKEN);
            if (cookieUid != null && cookiePass != null) {
                Long id = null;
                if (cookieUid.getValue() != null) {
                    try {
                        id = Long.parseLong(cookieUid.getValue());
                    } catch (NumberFormatException ignore) {
                    }
                }
                final String hash = cookiePass.getValue();
                UserData userData = null;

                if (id != null && hash != null) {
                    EntityManager em = null;
                    EntityTransaction tran = null;
                    try {
                        em = Database.createEntityManager();
                        tran = em.getTransaction();
                        tran.begin();
                        User user = em.find(User.class, id);
                        if (user != null) {
                            Secret secret = Secret.readData(user.getSecret());
                            if (secret.check(hash.toCharArray())) {
                                userData = new UserData(user);
                            }
                        }
                        tran.commit();
                    } catch (RuntimeException exc) {
                        if (tran != null && tran.isActive()) tran.rollback();
                        throw exc;
                    } finally {
                        if (em != null) em.close();
                    }
                }

                if (userData != null) {
                    cookieUid.setMaxAge(60 * 60 * 24 * 30);
                    cookieUid.setMaxAge(60 * 60 * 24 * 30);
                    hresp.addCookie(cookieUid);
                    hresp.addCookie(cookiePass);
                    hreq.setAttribute(ATTR_USERDATA, userData);
                    chain.doFilter(req, resp);
                    return;
                } else {
                    cookieUid.setValue("");
                    cookieUid.setMaxAge(0);
                    cookiePass.setValue("");
                    cookiePass.setMaxAge(0);
                    hresp.addCookie(cookieUid);
                    hresp.addCookie(cookiePass);
                }
            }
            hresp.sendRedirect("/login?error=1");
            return;
        }
        resp.flushBuffer();
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
