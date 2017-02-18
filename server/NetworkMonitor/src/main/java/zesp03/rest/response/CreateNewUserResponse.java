package zesp03.rest.response;

public class CreateNewUserResponse {
    private boolean success;
    private long userId;
    private long tokenId;
    private String tokenValue;
    private String activationURL;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getActivationURL() {
        return activationURL;
    }

    public void setActivationURL(String activationURL) {
        this.activationURL = activationURL;
    }
}
