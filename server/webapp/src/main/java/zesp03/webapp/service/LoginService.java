package zesp03.webapp.service;

import zesp03.webapp.dto.LoginResultDto;

public interface LoginService {
    LoginResultDto login(String userName, String password);
}
