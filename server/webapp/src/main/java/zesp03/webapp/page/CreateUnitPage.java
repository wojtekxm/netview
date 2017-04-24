package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreateUnitPage {
    @GetMapping("/create-unit")
    public String get() {
        return "create-unit";
    }
}
