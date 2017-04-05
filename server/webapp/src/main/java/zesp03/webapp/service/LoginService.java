package zesp03.webapp.service;

import zesp03.webapp.dto.LoginResultDto;
import zesp03.webapp.dto.UserDto;

public interface LoginService {
    // zwraca null jak się nie uda
    LoginResultDto login(String userName, String password);

    /**
     * @return null jeśli uwierzytelnianie się nie powiodło.
     */
    UserDto authenticate(Long userId, String passToken);
}
