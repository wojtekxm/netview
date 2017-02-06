package zesp03.data;

import zesp03.entity.User;

public class UserData {
    private long id;
    private String name;
    private byte[] secret;
    private boolean isAdmin;

    public UserData(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.secret = user.getSecret();
        this.isAdmin = user.isAdmin();
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
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
