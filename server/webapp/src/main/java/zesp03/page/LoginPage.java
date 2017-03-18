package zesp03.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import zesp03.dto.LoginResultDto;
import zesp03.exception.ValidationException;
import zesp03.filter.AuthenticationFilter;
import zesp03.service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;

@Controller
public class LoginPage {
    @PostMapping("/login")
    public String post(
            @FormParam("username") String username,
            @FormParam("password") String password,
            ModelMap model,
            HttpServletResponse resp) {
        if(username == null)
            throw new ValidationException("username", "null");//!?!
        if(password == null)
            throw new ValidationException("password", "null");//!?!
        LoginResultDto result = new LoginService().login(username, password);
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
