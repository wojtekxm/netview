package zesp03.webapp.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(LoginApi.class);

    private final LoginService loginService;

    @Autowired
    public LoginApi(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/api/login", consumes = "application/x-www-form-urlencoded")
    public ContentDto<AccessDto> login(
            @RequestParam("username") String userName,
            @RequestParam("password") String password,
            @RequestParam(value = "remember", required = false) String remember,
            HttpServletResponse resp) {
        ContentDto<AccessDto> result = ContentDto.make( () -> loginService.login(userName, password) );
        if(result.getContent() == null) {
            result.setSuccess(false);
            final Cookie cu = new Cookie(AuthenticationFilter.COOKIE_USERID,"");
            cu.setMaxAge(0);
            cu.setPath("/");
            resp.addCookie(cu);
            final Cookie cp = new Cookie(AuthenticationFilter.COOKIE_PASSTOKEN,"");
            cp.setMaxAge(0);
            cp.setPath("/");
            resp.addCookie(cp);
        }
        else {
            final int maxAge = (remember != null && remember.length() > 0) ? (60 * 60 * 24 * 30) : -1;
            log.debug("maxAge={}", maxAge);
            AccessDto access = result.getContent();

            final Cookie cu = new Cookie(
                    AuthenticationFilter.COOKIE_USERID,
                    Long.toString( access.getUserId() )
            );
            cu.setMaxAge(maxAge);
            cu.setPath("/");
            resp.addCookie(cu);
            final Cookie cp = new Cookie(
                    AuthenticationFilter.COOKIE_PASSTOKEN,
                    access.getPassToken()
            );
            cp.setMaxAge(maxAge);
            cp.setPath("/");
            resp.addCookie(cp);
        }
        return result;
    }
}
