package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import zesp03.webapp.filter.AuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomePage {
    @GetMapping("/")
    public String get(HttpServletRequest req) {
        if(req.getAttribute(AuthenticationFilter.ATTR_USERROW) != null)
            return "home-logged";
        else
            return "home-public";
    }
}
