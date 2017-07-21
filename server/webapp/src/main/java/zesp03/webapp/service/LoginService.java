/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.service;

import zesp03.common.entity.User;
import zesp03.webapp.dto.AccessDto;
import zesp03.webapp.dto.PasswordResetDto;
import zesp03.webapp.dto.UserDto;
import zesp03.webapp.dto.input.ChangePasswordDto;
import zesp03.webapp.dto.input.ResetPasswordDto;

public interface LoginService {
    AccessDto grantAccess(User user);

    boolean checkLogin(User user, String password);

    /**
     * @return null jeśli się nie uda
     */
    AccessDto login(String userName, String password);

    void logout(Long tokenId);

    /**
     * @return null jeśli uwierzytelnianie się nie powiodło.
     */
    UserDto authenticate(Long tokenId, String tokenValue);

    void changePassword(Long userId, ChangePasswordDto dto);

    void setPassword(User user, String password);

    PasswordResetDto beginResetPassword(Long userId, String serverName, int serverPort);

    void finishResetPassword(ResetPasswordDto dto);

    boolean checkResetPassword(Long tokenId, String tokenValue);

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
