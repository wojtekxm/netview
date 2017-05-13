package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import zesp03.webapp.filter.AuthenticationFilter;
import zesp03.webapp.service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LogoutPage {
    @Autowired
    private LoginService loginService;

    @GetMapping("/logout")
    public String get(HttpServletRequest req, HttpServletResponse resp) {
        loginService.logout(AuthenticationFilter.getTid(req));
        Cookie cu = new Cookie(AuthenticationFilter.COOKIE_USERID, "");
        cu.setPath("/");
        cu.setMaxAge(0);
        resp.addCookie(cu);
        Cookie cp = new Cookie(AuthenticationFilter.COOKIE_PASSTOKEN, "");
        cp.setPath("/");
        cp.setMaxAge(0);
        resp.addCookie(cp);
        return "redirect:/";
    }
}
