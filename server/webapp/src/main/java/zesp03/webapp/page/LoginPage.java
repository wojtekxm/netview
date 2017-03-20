package zesp03.webapp.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.webapp.dto.LoginResultDto;
import zesp03.webapp.filter.AuthenticationFilter;
import zesp03.webapp.service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginPage {
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);
    private final LoginService loginService;

    @Autowired
    public LoginPage(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public String post(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            ModelMap model,
            HttpServletResponse resp) {
        LoginResultDto result = loginService.login(username, password);
        if (result.isSuccess()) {
            Cookie cu = new Cookie(
                    AuthenticationFilter.COOKIE_USERID,
                    Long.toString(result.getUserId())
            );
            cu.setMaxAge(60 * 60 * 24 * 30);
            cu.setPath("/");
            resp.addCookie(cu);
            Cookie cp = new Cookie(
                    AuthenticationFilter.COOKIE_PASSTOKEN,
                    result.getPassToken()
            );
            cp.setMaxAge(60 * 60 * 24 * 30);
            cp.setPath("/");
            resp.addCookie(cp);
            return "redirect:/";
        } else {
            model.put("failed", true);
            return "login";
        }
    }

    @GetMapping("/login")
    public String get(HttpServletRequest req) {
        if(req.getAttribute(AuthenticationFilter.ATTR_USERROW) != null) {
            return "redirect:/";
        }
        return "login";
    }
}