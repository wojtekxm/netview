package zesp03.common;

public class RejectedValueException extends ApiException {
    private String field;

    public RejectedValueException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
