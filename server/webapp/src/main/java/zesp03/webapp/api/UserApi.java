/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.webapp.dto.PasswordResetDto;
import zesp03.webapp.dto.UserCreatedDto;
import zesp03.webapp.dto.UserDto;
import zesp03.webapp.dto.input.ActivateUserDto;
import zesp03.webapp.dto.input.ChangePasswordDto;
import zesp03.webapp.dto.input.CreateUserDto;
import zesp03.webapp.dto.input.ResetPasswordDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.filter.AuthenticationFilter;
import zesp03.webapp.service.LoginService;
import zesp03.webapp.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserApi {
    private static final Logger log = LoggerFactory.getLogger(UserApi.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/info/all")
    public ListDto<UserDto> getAll() {
        return ListDto.make( () -> userService.getAll() );
    }

    @GetMapping("/info/{userId}")
    public ContentDto<UserDto> getOne(
            @PathVariable("userId") long userId) {
        return ContentDto.make( () -> userService.getOne(userId) );
    }

    @PostMapping("/create")
    public ContentDto<UserCreatedDto> postCreate(
            @RequestBody CreateUserDto dto,
            HttpServletRequest request) {
        return ContentDto.make( () ->
                userService.create(
                        dto.getSendEmail(),
                        request.getServerName(),
                        request.getServerPort()
                )
        );
    }

    @PostMapping("/block/{userId}")
    public BaseResultDto postBlock(
            @PathVariable("userId") long userId,
            HttpServletRequest req) {
        return BaseResultDto.make( () ->
                userService.block(
                        userId,
                        AuthenticationFilter.getUser(req).getId()
                )
        );
    }

    @PostMapping("/unlock/{userId}")
    public BaseResultDto postUnlock(
            @PathVariable("userId") long userId,
            HttpServletRequest req) {
        return BaseResultDto.make( () ->
                userService.unlock(
                        userId,
                        AuthenticationFilter.getUser(req).getId()
                )
        );
    }

    @PostMapping("/advance/{userId}")
    public BaseResultDto postAdvance(
            @PathVariable("userId") long userId,
            HttpServletRequest req) {
        return BaseResultDto.make( () ->
                userService.advance(
                        userId,
                        AuthenticationFilter.getUser(req).getId()
                )
        );
    }

    @PostMapping("/degrade/{userId}")
    public BaseResultDto postDegrade(
            @PathVariable("userId") long userId,
            HttpServletRequest req) {
        return BaseResultDto.make( () ->
                userService.degrade(
                        userId,
                        AuthenticationFilter.getUser(req).getId()
                )
        );
    }

    @PostMapping("/remove/{userId}")
    public BaseResultDto remove(
            @PathVariable("userId") long userId,
            HttpServletRequest req) {
        return BaseResultDto.make( () ->
                userService.remove(
                        userId,
                        AuthenticationFilter.getUser(req).getId()
                )
        );
    }

    @PostMapping("/change-password")
    public BaseResultDto changePassword(
            @RequestBody ChangePasswordDto dto,
            HttpServletRequest req) {
        return BaseResultDto.make( () ->
                loginService.changePassword(
                        AuthenticationFilter.getUser(req).getId(),
                        dto
                )
        );
    }

    @PostMapping("/begin-reset-password/{userId}")
    public ContentDto<PasswordResetDto> postResetPassword(
            @PathVariable("userId") long userId,
            HttpServletRequest request) {
        return ContentDto.make( () ->
                loginService.beginResetPassword(
                        userId,
                        request.getServerName(),
                        request.getServerPort()
                )
        );
    }

    @PostMapping("/finish-reset-password")
    public BaseResultDto postFinishResetPassword(
            @RequestBody ResetPasswordDto dto) {
        return BaseResultDto.make( () -> loginService.finishResetPassword(dto) );
    }

    @PostMapping("/activate")
    public BaseResultDto activate(
            @RequestBody ActivateUserDto dto) {
        return BaseResultDto.make( () -> userService.activate(dto) );
    }
}
