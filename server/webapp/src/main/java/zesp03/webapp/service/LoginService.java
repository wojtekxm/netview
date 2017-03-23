package zesp03.webapp.service;

import zesp03.webapp.dto.UserDto;
import zesp03.webapp.dto.result.LoginResultDto;

public interface LoginService {
    LoginResultDto login(String userName, String password);

    /**
     * @return null jeśli uwierzytelnianie się nie powiodło.
     */
    UserDto authenticate(Long userId, String passToken);
}
