package zesp03.data.row;

import zesp03.entity.User;

public class UserRow {
    private long id;
    private String name;
    private byte[] secret;
    private boolean admin;

    public UserRow() {
    }

    /**
     * User entity should be in managed state.
     */
    public UserRow(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.secret = user.getSecret();
        this.admin = user.isAdmin();
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
