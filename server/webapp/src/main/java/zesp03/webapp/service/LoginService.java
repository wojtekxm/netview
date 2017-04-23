package zesp03.webapp.service;

import zesp03.webapp.dto.AccessDto;
import zesp03.webapp.dto.UserDto;

public interface LoginService {
    /**
     * @return null jeśli się nie uda
     */
    AccessDto login(String userName, String password);

    /**
     * @return null jeśli uwierzytelnianie się nie powiodło.
     */
    UserDto authenticate(Long userId, String passToken);
}
