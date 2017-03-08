package zesp03.common;

public class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(message);
    }
}
