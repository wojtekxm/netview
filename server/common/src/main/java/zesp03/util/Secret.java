package zesp03.util;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * Obiekty tej klasy są niezmienne i bezpieczne w wielu wątkach
 */
public final class Secret {
    public static final int ITERATIONS_BYTES = 4;
    public static final int SALT_BYTES = 64;
    public static final int KEY_BYTES = 64;
    public static final int ALL_BYTES = ITERATIONS_BYTES + SALT_BYTES + KEY_BYTES;

    /**
     * @param password   hasło, nie może być puste
     * @param iterations liczba iteracji, musi być dodatnia
     * @return sekret utworzony dla danego hasła i liczby iteracji
     */
    public static Secret create(char[] password, int iterations) {
        if (password.length < 1)
            throw new IllegalArgumentException("password.length < 1");
        if (iterations < 1)
            throw new IllegalArgumentException("iterations < 1");
        try {
            final byte[] salt = new byte[SALT_BYTES];
            new SecureRandom().nextBytes(salt);
            final PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_BYTES * 8);
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            final byte[] key = factory.generateSecret(spec).getEncoded();
            return new Secret(iterations, salt, key);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exc) {
            throw new IllegalStateException(exc);
        }
    }

    public static Secret readData(byte[] data) {
        return new Secret(data);
    }

    public static boolean check(byte[] secretData, String password) {
        return Secret.readData(secretData).check(password.toCharArray());
    }

    private final byte[] data;

    protected Secret(int iterations, byte[] salt, byte[] key) {
        if (iterations < 1)
            throw new IllegalArgumentException("iterations < 1");
        if (salt.length != SALT_BYTES)
            throw new IllegalArgumentException("salt.length != SALT_BYTES");
        if (key.length != KEY_BYTES)
            throw new IllegalArgumentException("key.length != KEY_BYTES");

        this.data = new byte[ALL_BYTES];
        System.arraycopy(Bytes.encodeInt(iterations), 0, data, 0, ITERATIONS_BYTES);
        System.arraycopy(salt, 0, data, ITERATIONS_BYTES, SALT_BYTES);
        System.arraycopy(key, 0, data, ITERATIONS_BYTES + SALT_BYTES, KEY_BYTES);
    }

    protected Secret(byte[] src) {
        if (src.length != ALL_BYTES)
            throw new IllegalArgumentException("src.length != ALL_BYTES");
        this.data = Arrays.copyOf(src, ALL_BYTES);
    }

    /**
     * @return kopia danych sekretu
     */
    public byte[] getData() {
        return Arrays.copyOf(data, ALL_BYTES);
    }

    protected int getIterations() {
        return Bytes.decodeInt(data);
    }

    protected byte[] getSalt() {
        return Arrays.copyOfRange(data, ITERATIONS_BYTES, ITERATIONS_BYTES + SALT_BYTES);
    }

    protected byte[] getKey() {
        return Arrays.copyOfRange(data, ITERATIONS_BYTES + SALT_BYTES, ITERATIONS_BYTES + SALT_BYTES + KEY_BYTES);
    }

    /**
     * @param password hasło do sprawdzenia
     * @return true jeśli password jest poprawnym hasłem
     */
    public boolean check(char[] password) {
        if (password.length < 1)
            throw new IllegalArgumentException("password.length < 1");
        try {
            final int iterations = getIterations();
            final byte[] salt = getSalt();
            final PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_BYTES * 8);
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            final byte[] actualKey = factory.generateSecret(spec).getEncoded();
            final byte[] expectedKey = getKey();
            return Arrays.equals(expectedKey, actualKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exc) {
            throw new IllegalStateException(exc);
        }
    }
}