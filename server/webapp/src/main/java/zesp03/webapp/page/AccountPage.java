package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountPage {
    @GetMapping("/account")
    public String get() {
        return "account";
    }
}
