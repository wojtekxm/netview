package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.webapp.dto.PasswordChangedDto;
import zesp03.webapp.dto.UserCreatedDto;
import zesp03.webapp.dto.UserDto;
import zesp03.webapp.dto.input.ActivateUserDto;
import zesp03.webapp.dto.input.ChangePasswordDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.filter.AuthenticationFilter;
import zesp03.webapp.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserApi {
    @Autowired
    private UserService userService;

    @GetMapping("/api/user/all")
    public ListDto<UserDto> getAll() {
        return ListDto.make( () -> userService.getAll() );
    }

    @GetMapping("/api/user")
    public ContentDto<UserDto> getOne(
            @RequestParam("id") long id) {
        return ContentDto.make( () -> userService.getOne(id) );
    }

    @PostMapping(value = "/api/user/block/{userId}")
    public BaseResultDto block(
            @PathVariable("userId") long id) {
        return BaseResultDto.make( () -> userService.block(id) );
    }

    @PostMapping(value = "/api/user/create")
    public ContentDto<UserCreatedDto> create(HttpServletRequest request) {
        return ContentDto.make( () -> userService.create( request.getServerName(), request.getServerPort() ) );
    }

    @PostMapping(value = "/api/user/remove/{userId}")
    public BaseResultDto remove(
            @PathVariable("userId") long id) {
        return BaseResultDto.make( () -> userService.remove(id) );
    }

    @PostMapping(value = "/api/change-password", consumes = "application/x-www-form-urlencoded")
    public ContentDto<PasswordChangedDto> changePassword(
            @RequestParam("old") String old,
            @RequestParam("desired") String desired,
            @RequestParam("repeat") String repeat,
            HttpServletRequest req) {
        return ContentDto.make( () -> {
            ChangePasswordDto dto = new ChangePasswordDto();
            dto.setOld(old);
            dto.setDesired(desired);
            dto.setRepeat(repeat);
            UserDto user = (UserDto)req.getAttribute(AuthenticationFilter.ATTR_USERDTO);
            return userService.changePassword(user.getId(), dto);
        } );
    }

    @PostMapping(value = "/api/change-password", consumes = "application/json")
    public ContentDto<PasswordChangedDto> changePassword(
            @RequestBody ChangePasswordDto dto,
            HttpServletRequest req) {
        return ContentDto.make( () -> {
            UserDto user = (UserDto)req.getAttribute(AuthenticationFilter.ATTR_USERDTO);
            return userService.changePassword(user.getId(), dto);
        } );
    }

    @PostMapping(value = "/api/user/activate", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto activate(
            @RequestParam("tid") long tokenId,
            @RequestParam("tv") String tokenValue,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("repeat") String repeatPassword) {
        return BaseResultDto.make( () -> {
            ActivateUserDto dto = new ActivateUserDto();
            dto.setTokenId(tokenId);
            dto.setTokenValue(tokenValue);
            dto.setUsername(username);
            dto.setPassword(password);
            dto.setRepeatPassword(repeatPassword);
            userService.activate(dto);
        } );
    }

    @PostMapping(value = "/api/user/activate", consumes = "application/json")
    public BaseResultDto activate(
            @RequestBody ActivateUserDto dto) {
        return BaseResultDto.make( () -> userService.activate(dto) );
    }
}
