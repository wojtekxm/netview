package zesp03.dto;

public class LoginResultDto extends BaseResultDto {
    private long userId;
    private String passToken;

    public LoginResultDto(boolean success) {
        super(success);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPassToken() {
        return passToken;
    }

    public void setPassToken(String passToken) {
        this.passToken = passToken;
    }
}
