/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.exception.AccessException;
import zesp03.webapp.service.UserService;

@Controller
public class ActivateAccountPage {
    @Autowired
    private UserService userService;

    @GetMapping("/activate-account")
    public String get(
            @RequestParam("tid") long tokenId,
            @RequestParam("tv") String tokenValue) {
        if(! userService.checkActivation(tokenId, tokenValue)) {
            throw new AccessException("invalid token");
        }
        return "activate-account";
    }
}
