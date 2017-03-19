package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import zesp03.webapp.config.Cookies;
import zesp03.webapp.filter.AuthenticationFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LogoutPage {
    @GetMapping("/logout")
    public String get(HttpServletRequest req, HttpServletResponse resp) {
        Cookie cu = Cookies.find(req, AuthenticationFilter.COOKIE_USERID);
        if (cu != null) {
            cu.setValue("");
            cu.setMaxAge(0);
            resp.addCookie(cu);
        }
        Cookie cp = Cookies.find(req, AuthenticationFilter.COOKIE_PASSTOKEN);
        if (cp != null) {
            cp.setValue("");
            cp.setMaxAge(0);
            resp.addCookie(cp);
        }
        return "redirect:/";
    }
}
