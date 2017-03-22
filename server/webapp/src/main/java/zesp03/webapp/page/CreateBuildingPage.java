package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Berent on 2017-03-21.
 */
@Controller
public class CreateBuildingPage {
    @GetMapping("/create-building")
    public String get() {
        return "create-building";
    }
}
