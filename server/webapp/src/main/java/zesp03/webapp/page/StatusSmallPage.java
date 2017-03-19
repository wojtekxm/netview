package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusSmallPage {
    @GetMapping("/status-small")
    public String get() {
        return "status-small";
    }
}
