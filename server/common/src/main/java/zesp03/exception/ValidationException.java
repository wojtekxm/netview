package zesp03.exception;

public class ValidationException extends BaseException {
    private final String field, reason;

    public ValidationException(String field, String reason) {
        super("invalid field: " + field + ", reason: " + reason);
        this.field = field;
        this.reason = reason;
    }

    public String getField() {
        return field;
    }

    public String getReason() {
        return reason;
    }
}
