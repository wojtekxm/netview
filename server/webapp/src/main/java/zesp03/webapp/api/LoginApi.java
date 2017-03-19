package zesp03.webapp.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.webapp.dto.LoginResultDto;
import zesp03.webapp.service.LoginService;

@RestController
public class LoginApi {
    @PostMapping(value = "/api/login", consumes = "application/x-www-form-urlencoded")
    public LoginResultDto login(
            @RequestParam(value = "username", required = true) String userName,
            @RequestParam(value = "password", required = true) String password) {
        LoginResultDto result = new LoginService().login(userName, password);
        //TODO set cookies ?
        return result;
    }
}
