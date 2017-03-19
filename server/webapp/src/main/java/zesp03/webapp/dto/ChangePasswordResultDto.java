package zesp03.webapp.dto;

public class ChangePasswordResultDto extends BaseResultDto {
    private String passToken;

    public ChangePasswordResultDto(boolean success) {
        super(success);
    }

    public String getPassToken() {
        return passToken;
    }

    public void setPassToken(String passToken) {
        this.passToken = passToken;
    }
}
