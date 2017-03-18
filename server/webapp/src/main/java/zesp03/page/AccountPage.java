package zesp03.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountPage {
    @GetMapping("/account")
    public String get() {
        return "account";
    }
}
