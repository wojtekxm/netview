package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.webapp.dto.LoginResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.service.LoginService;

@RestController
public class LoginApi {
    private final LoginService loginService;

    @Autowired
    public LoginApi(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/api/login", consumes = "application/x-www-form-urlencoded")
    public ContentDto<LoginResultDto> login(
            @RequestParam("username") String userName,
            @RequestParam("password") String password) {
        //TODO set cookies ?
        ContentDto<LoginResultDto> result = ContentDto.make( () -> loginService.login(userName, password) );
        if(result.getContent() == null) {
            result.setSuccess(false);
        }
        return result;
    }
}
