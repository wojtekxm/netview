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
