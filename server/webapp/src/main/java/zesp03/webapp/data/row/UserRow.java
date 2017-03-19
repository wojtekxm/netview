package zesp03.webapp.data.row;

import zesp03.common.entity.User;
import zesp03.common.entity.UserRole;

public class UserRow {
    private long id;
    private String name;
    private byte[] secret;
    private boolean activated;
    private boolean blocked;
    private UserRole role;

    public UserRow() {
    }

    /**
     * User entity should be in managed state.
     */
    public UserRow(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.secret = user.getSecret();
        this.activated = user.isActivated();
        this.blocked = user.isBlocked();
        this.role = user.getRole();
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

    public byte[] getSecret() {
        return secret;
    }

    public void setSecret(byte[] secret) {
        this.secret = secret;
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
