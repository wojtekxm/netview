/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.config;

import zesp03.webapp.service.LoginService;

@Deprecated
public class UglyUserServiceHolder {
    private static LoginService loginService;

    public static synchronized LoginService getLoginService() {
        return loginService;
    }

    public static synchronized void setLoginService(LoginService loginService) {
        UglyUserServiceHolder.loginService = loginService;
    }
}
