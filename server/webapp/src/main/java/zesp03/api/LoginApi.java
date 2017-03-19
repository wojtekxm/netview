package zesp03.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.dto.LoginResultDto;
import zesp03.service.LoginService;

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
