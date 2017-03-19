package zesp03.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.exception.AccessException;
import zesp03.service.UserService;

@Controller
public class ActivateAccountPage {
    @GetMapping("/activate-account")
    public String get(
            @RequestParam("tid") long tokenId,
            @RequestParam("tv") String tokenValue) {
        if(! new UserService().checkActivation(tokenId, tokenValue)) {
            throw new AccessException("invalid token");
        }
        return "activate-account";
    }
}
