package zesp03.webapp.dto;

import zesp03.common.entity.User;
import zesp03.common.entity.UserRole;

public class UserDto {
    private long id;
    private String name;
    private boolean activated;
    private boolean blocked;
    private UserRole role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void wrap(User u) {
        this.id = u.getId();
        this.name = u.getName();
        this.activated = u.isActivated();
        this.blocked = u.isBlocked();
        this.role = u.getRole();
    }

    public static UserDto make(User u) {
        UserDto dto = new UserDto();
        dto.wrap(u);
        return dto;
    }
}
