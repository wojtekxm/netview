package zesp03.util;

public class Bytes {
    /**
     * @param arr tablica conajmniej 4 bajtów
     * @return wartość odczytana z pierwszych 4 bajtów tablicy arr w stylu Little Endian
     */
    public static int decodeInt(byte[] arr) {
        if (arr.length < 4)
            throw new IllegalArgumentException("arr.length < 4");
        int value = 0;
        value |= arr[0] & 0xff;
        value |= (arr[1] << 8) & 0xff00;
        value |= (arr[2] << 16) & 0xff0000;
        value |= (arr[3] << 24) & 0xff000000;
        return value;
    }

    /**
     * @param value wartość do zakodowania
     * @return tablica 4 bajtów, wartość value skonwertowana do bajtów w stylu Little Endian
     */
    public static byte[] encodeInt(int value) {
        final byte[] arr = new byte[4];
        arr[0] = (byte) (value);
        arr[1] = (byte) (value >> 8);
        arr[2] = (byte) (value >> 16);
        arr[3] = (byte) (value >> 24);
        return arr;
    }
}
