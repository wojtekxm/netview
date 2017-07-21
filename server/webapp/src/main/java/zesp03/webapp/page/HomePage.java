/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import zesp03.webapp.filter.AuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomePage {
    @GetMapping("/")
    public String get(HttpServletRequest req) {
        if(AuthenticationFilter.getUser(req) != null)
            return "status";
        else
            return "home-public";
    }
}
