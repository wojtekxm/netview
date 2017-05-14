package zesp03.webapp.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.webapp.dto.AccessDto;
import zesp03.webapp.filter.AuthenticationFilter;
import zesp03.webapp.service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginPage {
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public String post(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "remember", required = false) String remember,
            ModelMap model,
            HttpServletResponse resp) {
        AccessDto result = loginService.login(username, password);
        if (result != null) {
            final int maxAge = (remember != null && remember.length() > 0) ? (60 * 60 * 24 * 30) : -1;
            final Cookie cu = new Cookie(
                    AuthenticationFilter.COOKIE_USERID,
                    Long.toString( result.getUserId() )
            );
            cu.setMaxAge(maxAge);
            cu.setPath("/");
            resp.addCookie(cu);
            final Cookie cp = new Cookie(
                    AuthenticationFilter.COOKIE_PASSTOKEN,
                    result.getPassToken()
            );
            cp.setMaxAge(maxAge);
            cp.setPath("/");
            resp.addCookie(cp);

            return "redirect:/";
        } else {
            final Cookie cu = new Cookie(AuthenticationFilter.COOKIE_USERID,"");
            cu.setMaxAge(0);
            cu.setPath("/");
            resp.addCookie(cu);
            final Cookie cp = new Cookie(AuthenticationFilter.COOKIE_PASSTOKEN,"");
            cp.setMaxAge(0);
            cp.setPath("/");
            resp.addCookie(cp);

            model.put("failed", true);
            return "home-public";
        }
    }

    @GetMapping("/login")
    public String get(HttpServletRequest req) {
        if(AuthenticationFilter.getUser(req) != null) {
            return "redirect:/";
        }
        return "home-public";
    }
}
