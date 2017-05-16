package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.exception.AccessException;
import zesp03.webapp.filter.AuthenticationFilter;
import zesp03.webapp.service.LoginService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ResetPasswordPage {
    @Autowired
    private LoginService loginService;

    @GetMapping("/reset-password")
    public String getResetPassword(
            @RequestParam("tid") long tid,
            @RequestParam("tv") String tv,
            HttpServletRequest req) {
        if(AuthenticationFilter.getUser(req) != null) {
            return "redirect:/";
        }
        if(! loginService.checkResetPassword(tid, tv)) {
            throw new AccessException("invalid token");
        }
        return "reset-password";
    }
}
