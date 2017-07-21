/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import zesp03.webapp.dto.UserDto;
import zesp03.webapp.filter.AuthenticationFilter;
import zesp03.webapp.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserPage {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/user/{userId}")
    public String get(
            @PathVariable("userId") long userId,
            ModelMap model) {
        UserDto dto = userService.getOne(userId);
        model.put("selected", dto);
        model.put("id", userId);
        return "user";
    }

    @PostMapping("/user/remove/{userId}")
    public String postRemove(
            @PathVariable("userId") long userId,
            HttpServletRequest req) {
        userService.remove(userId, AuthenticationFilter.getUser(req).getId());
        return "redirect:/all-users";
    }
}
