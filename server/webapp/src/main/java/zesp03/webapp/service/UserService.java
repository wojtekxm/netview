package zesp03.webapp.service;

public interface UserService {
    /**
     * @param userName nazwa użytkownika którą ma mieć root.
     * @return id roota, który na pewno jest w bazie.
     */
    long makeRoot(String userName);

    void setPassword(long userId, String password);

    /**
     * @param userId id użytkownika
     * @param old stare hasło
     * @param desired nowe hasło
     * @param repeat powtórzenie nowego hasła
     * @return Passtoken dla nowego hasła jeśli uda się zmienić hasło (jak się nie uda to wyrzuca wyjątek).
     */
    String changePassword(long userId, String old, String desired, String repeat);

    boolean checkActivation(Long tokenId, String tokenValue);

    void activateAccount(Long tokenId, String tokenValue, String userName, String password, String repeatPassword);
}
