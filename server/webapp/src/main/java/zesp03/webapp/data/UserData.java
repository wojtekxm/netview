package zesp03.webapp.data;

import zesp03.common.entity.User;
import zesp03.common.entity.UserRole;
import zesp03.webapp.data.row.UserRow;

public class UserData {
    private long id;
    private String name;
    private boolean activated;
    private boolean blocked;
    private UserRole role;

    public UserData() {
    }

    public UserData(UserRow u) {
        this.id = u.getId();
        this.name = u.getName();
        this.activated = u.isActivated();
        this.blocked = u.isBlocked();
        this.role = u.getRole();
    }

    /**
     * User entity should be in managed state.
     */
    public UserData(User u) {
        this.id = u.getId();
        this.name = u.getName();
        this.activated = u.isActivated();
        this.blocked = u.isBlocked();
        this.role = u.getRole();
    }

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
}
