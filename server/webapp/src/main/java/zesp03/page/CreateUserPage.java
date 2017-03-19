package zesp03.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreateUserPage {
    @GetMapping("/create-user")
    public String get() {
        return "create-user";
    }
}
