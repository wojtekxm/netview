package zesp03.webapp.service;

import zesp03.webapp.dto.AccessDto;
import zesp03.webapp.dto.UserCreatedDto;
import zesp03.webapp.dto.UserDto;
import zesp03.webapp.dto.input.ActivateUserDto;
import zesp03.webapp.dto.input.ChangePasswordDto;

import java.util.List;

public interface UserService {
    UserDto getOne(Long userId);

    List<UserDto> getAll();

    void block(Long userId);

    void remove(Long userId);

    // w zwróconym dto activationURL będzie null
    UserCreatedDto create(String serverName, int serverPort);

    /**
     * @param userName nazwa użytkownika którą ma mieć root.
     * @return id roota, który na pewno jest w bazie.
     */
    long makeRoot(String userName);

    void setPassword(Long userId, String password);

    /**
     * @return Passtoken dla nowego hasła jeśli uda się zmienić hasło (jak się nie uda to wyrzuca wyjątek).
     */
    AccessDto changePassword(Long userId, ChangePasswordDto dto);

    boolean checkActivation(Long tokenId, String tokenValue);

    void activate(ActivateUserDto dto);

    /**
     * @param password hasło dla którego ma być wyznaczony hash
     * @return hash funkcji SHA-256 dla podanego hasła, zaprezentowany w formacie Base64URL.
     */
    String passwordToHash(String password);

    /**
     * Generuje losowy token w formacie Base64URL, reprezentujący ciąg bajtów o długości randomBytes.
     *
     * @param randomBytes liczba losowych bajtów do wygenerowania
     * @return napis Base64URL reprezentujący losowo wygenerowany token.
     */
    String generateToken(int randomBytes);
}
