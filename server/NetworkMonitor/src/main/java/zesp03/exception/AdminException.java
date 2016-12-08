package zesp03.exception;

/**
 * Ten wyjątek sygnalizuje że pewnej operacji administracyjnej nie dało się wykonać,
 * na przykład dlatego że jest sprzeczna z założeniami aplikacji, lub z powodu braku odpowiednich uprawnień.
 */
public class AdminException extends Exception {
    public AdminException(String message) {
        super(message);
    }
}
