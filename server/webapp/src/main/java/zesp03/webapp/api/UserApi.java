package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.core.App;
import zesp03.common.core.Database;
import zesp03.common.entity.Token;
import zesp03.common.entity.TokenAction;
import zesp03.common.entity.User;
import zesp03.common.entity.UserRole;
import zesp03.common.exception.NotFoundException;
import zesp03.common.util.Secret;
import zesp03.webapp.dto.UserDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ChangePasswordResultDto;
import zesp03.webapp.dto.result.CreateNewUserDto;
import zesp03.webapp.filter.AuthenticationFilter;
import zesp03.webapp.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserApi {
    private final UserService userService;

    @Autowired
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/all-users")
    public List<UserDto> getAllUsers() {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List<UserDto> list = em.createQuery("SELECT u FROM User u", User.class)
                    .getResultList()
                    .stream()
                    .map(UserDto::make)
                    .collect(Collectors.toList());
            tran.commit();
            return list;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @GetMapping("/api/user")
    public UserDto getUser(
            @RequestParam("id") long id) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = em.find(User.class, id);
            if(u == null)
                throw new NotFoundException("user");
            UserDto dto = UserDto.make(u);
            tran.commit();
            return dto;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @PostMapping(value = "/api/block-user", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto block(
            @RequestParam("id") long id) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = em.find(User.class, id);
            if (u == null)
                throw new NotFoundException("user");
            u.setBlocked(true);
            em.merge(u);
            tran.commit();
            return new BaseResultDto(true);
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @PostMapping(value = "/api/create-user", consumes = "application/x-www-form-urlencoded")
    public CreateNewUserDto createNewUser(
            @RequestParam("is-admin") boolean isAdmin,
            HttpServletRequest request) {
        String tokenValue = App.generateToken(32);
        Secret secret = Secret.create(tokenValue.toCharArray(), 1);

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = new User();
            u.setName(null);
            u.setSecret(null);
            u.setActivated(false);
            u.setBlocked(true);
            u.setRole(isAdmin ? UserRole.ADMIN : UserRole.NORMAL);
            em.persist(u);

            Token t = new Token();
            t.setSecret(secret.getData());
            t.setAction(TokenAction.ACTIVATE_ACCOUNT);
            t.setUser(u);
            em.persist(t);

            CreateNewUserDto r = new CreateNewUserDto();
            r.setTokenId(t.getId());
            r.setTokenValue(tokenValue);
            r.setUserId(u.getId());
            r.setActivationURL(
                    new StringBuilder()
                            .append("http://")
                            .append(request.getServerName())
                            .append(request.getServerPort() != 80 ? ":" + request.getServerPort() : "")
                            .append("/activate-account?tid=")
                            .append(r.getTokenId())
                            .append("&tv=")
                            .append(r.getTokenValue())
                            .toString()
            );
            tran.commit();
            return r;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @PostMapping(value = "/api/remove-user", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto removeUser(
            @RequestParam("id") long id) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = em.find(User.class, id);
            if (u == null)
                throw new NotFoundException("user");
            em.remove(u);
            tran.commit();
            return new BaseResultDto(true);
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @PostMapping(value = "/api/change-password", consumes = "application/x-www-form-urlencoded")
    public ChangePasswordResultDto post(
            @RequestParam("old") String old,
            @RequestParam("desired") String desired,
            @RequestParam("repeat") String repeat,
            HttpServletRequest req) {
        UserDto user = (UserDto)req.getAttribute(AuthenticationFilter.ATTR_USERDTO);
        String passtoken = userService.changePassword(user.getId(), old, desired, repeat);
        ChangePasswordResultDto result = new ChangePasswordResultDto();
        result.setPassToken(passtoken);
        return result;
    }

    @PostMapping(value = "/api/activate-account", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto activate(
            @RequestParam("tid") long tokenId,
            @RequestParam("tv") String tokenValue,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("repeat") String repeatPassword) {
        userService.activateAccount(tokenId, tokenValue, username, password, repeatPassword);
        return new BaseResultDto(true);
    }
}
