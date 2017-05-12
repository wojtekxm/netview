package zesp03.webapp.service;

import zesp03.webapp.dto.UserCreatedDto;
import zesp03.webapp.dto.UserDto;
import zesp03.webapp.dto.input.ActivateUserDto;

import java.util.List;

public interface UserService {
    UserDto getOne(Long userId);

    List<UserDto> getAll();

    void block(Long userId, Long callerId);
    void unlock(Long userId, Long callerId);

    void remove(Long userId, Long callerId);

    // w zwróconym dto activationURL będzie null
    UserCreatedDto create(String serverName, int serverPort);

    /**
     * @param userName nazwa użytkownika którą ma mieć root.
     */
    void makeRoot(String userName, String password);

    boolean checkActivation(Long tokenId, String tokenValue);

    void activate(ActivateUserDto dto);
}
