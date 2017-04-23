package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.webapp.dto.AccessDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.filter.AuthenticationFilter;
import zesp03.webapp.service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginApi {
    private final LoginService loginService;

    @Autowired
    public LoginApi(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/api/login", consumes = "application/x-www-form-urlencoded")
    public ContentDto<AccessDto> login(
            @RequestParam("username") String userName,
            @RequestParam("password") String password,
            HttpServletResponse resp) {
        ContentDto<AccessDto> result = ContentDto.make( () -> loginService.login(userName, password) );
        if(result.getContent() == null) {
            result.setSuccess(false);
        }
        else {
            AccessDto access = result.getContent();
            Cookie cu = new Cookie(
                    AuthenticationFilter.COOKIE_USERID,
                    Long.toString( access.getUserId() )
            );
            cu.setMaxAge(60 * 60 * 24 * 30);
            cu.setPath("/");
            resp.addCookie(cu);
            Cookie cp = new Cookie(
                    AuthenticationFilter.COOKIE_PASSTOKEN,
                    access.getPassToken()
            );
            cp.setMaxAge(60 * 60 * 24 * 30);
            cp.setPath("/");
            resp.addCookie(cp);
        }
        return result;
    }
}
