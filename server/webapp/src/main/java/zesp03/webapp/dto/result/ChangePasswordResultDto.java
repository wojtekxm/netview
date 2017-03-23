package zesp03.webapp.dto.result;

public class ChangePasswordResultDto extends BaseResultDto {
    private String passToken;

    public String getPassToken() {
        return passToken;
    }

    public void setPassToken(String passToken) {
        this.passToken = passToken;
    }
}
