package zesp03.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreateControllerPage {
    @GetMapping("/create-controller")
    public String get() {
        return "create-controller";
    }
}
