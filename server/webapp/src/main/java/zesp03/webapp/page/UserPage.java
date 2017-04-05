package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.webapp.dto.UserDto;
import zesp03.webapp.service.UserService;

@Controller
public class UserPage {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/user")
    public String get(
            @RequestParam(value="id") long id,
            ModelMap model) {
        UserDto dto = userService.getOne(id);
        model.put("selected", dto);
        return "user";
    }
}
