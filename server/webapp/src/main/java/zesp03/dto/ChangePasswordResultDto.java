package zesp03.dto;

public class ChangePasswordResultDto {
    public enum ErrorReason {
        INVALID_OLD_PASSWORD,
        REJECTED_NEW_PASSWORD,
        PASSWORDS_DONT_MATCH
    }

    private ErrorReason reason;
    private boolean success;
    private String passToken;

    public ErrorReason getReason() {
        return reason;
    }

    public void setReason(ErrorReason reason) {
        this.reason = reason;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getPassToken() {
        return passToken;
    }

    public void setPassToken(String passToken) {
        this.passToken = passToken;
    }
}
