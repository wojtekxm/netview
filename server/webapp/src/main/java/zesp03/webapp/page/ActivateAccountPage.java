package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.exception.AccessException;
import zesp03.webapp.service.UserService;

@Controller
public class ActivateAccountPage {
    private final UserService userService;

    @Autowired
    public ActivateAccountPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/activate-account")
    public String get(
            @RequestParam("tid") long tokenId,
            @RequestParam("tv") String tokenValue) {
        if(! userService.checkActivation(tokenId, tokenValue)) {
            throw new AccessException("invalid token");
        }
        return "activate-account";
    }
}