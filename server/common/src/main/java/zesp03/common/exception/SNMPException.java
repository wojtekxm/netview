package zesp03.common.exception;

public class SNMPException extends BaseException {
    public SNMPException(String message) {
        super(message);
    }

    public SNMPException(String message, Throwable cause) {
        super(message, cause);
    }
}
