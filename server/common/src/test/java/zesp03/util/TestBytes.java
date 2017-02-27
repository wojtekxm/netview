package zesp03.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestBytes {
    @Test
    public void decodeInt_0() {
        final byte[] arr = {0, 0, 0, 0};
        final int value = Bytes.decodeInt(arr);
        assertEquals(0, value);
    }

    @Test
    public void decodeInt_0x01020304() {
        final byte[] arr = {4, 3, 2, 1};
        final int value = Bytes.decodeInt(arr);
        assertEquals(0x01020304, value);
    }

    @Test
    public void decodeInt_0xfffefdfc() {
        final byte[] arr = {-4, -3, -2, -1};
        final int value = Bytes.decodeInt(arr);
        assertEquals(0xfffefdfc, value);
    }

    @Test
    public void decodeInt_minus1() {
        final byte[] arr = {-1, -1, -1, -1};
        final int value = Bytes.decodeInt(arr);
        assertEquals(-1, value);
    }

    @Test
    public void encodeInt_0() {
        final byte[] arr = Bytes.encodeInt(0);
        final byte[] exp = {0, 0, 0, 0};
        assertArrayEquals(exp, arr);
    }

    @Test
    public void encodeInt_0x01020304() {
        final byte[] arr = Bytes.encodeInt(0x01020304);
        final byte[] exp = {4, 3, 2, 1};
        assertArrayEquals(exp, arr);
    }

    @Test
    public void encodeInt_0xfffefdfc() {
        final byte[] arr = Bytes.encodeInt(0xfffefdfc);
        final byte[] exp = {-4, -3, -2, -1};
        assertArrayEquals(exp, arr);
    }

    @Test
    public void encodeInt_minus1() {
        final byte[] arr = Bytes.encodeInt(-1);
        final byte[] exp = {-1, -1, -1, -1};
        assertArrayEquals(exp, arr);
    }
}
