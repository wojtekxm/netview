/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
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
    void advance(Long userId, Long callerId);
    void degrade(Long userId, Long callerId);

    void remove(Long userId, Long callerId);

    /**
     * @param sendEmail jeśli nie null, zostanie wysłany e-mail z linkiem aktywacyjnym na ten adres
     * @param serverName adres serwera na który ma prowadzić link aktywacyjny
     * @param serverPort port na docelowym serwerze
     */
    UserCreatedDto create(String sendEmail, String serverName, int serverPort);

    /**
     * @param userName nazwa użytkownika którą ma mieć root.
     */
    void makeRoot(String userName, String password);

    boolean checkActivation(Long tokenId, String tokenValue);

    void activate(ActivateUserDto dto);
}
