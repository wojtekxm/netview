package zesp03.common;

import org.junit.Test;
import zesp03.util.TestUnicode;

import static org.junit.Assert.*;

public class TestApp {
    private static final String TOO_LONG = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb" +
            "cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc" +
            "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";

    @Test
    public void isCompatibleDeviceName_abc_true() {
        assertTrue(App.isCompatibleDeviceName("abc"));
    }

    @Test
    public void isCompatibleDeviceName_empty_true() {
        assertTrue(App.isCompatibleDeviceName(""));
    }

    @Test
    public void isCompatibleDeviceName_tooLong_false() {
        assertFalse(App.isCompatibleDeviceName(TOO_LONG));
    }

    @Test
    public void isCompatibleDeviceName_withSurrogates_false() {
        String s = "1" + TestUnicode.BAT_STRING + "2";
        assertFalse(App.isCompatibleDeviceName(s));
    }

    @Test
    public void getCompatibleDeviceName_valid_theSame() {
        assertEquals(App.getCompatibleDeviceName("123"), "123");
    }

    @Test
    public void getCompatibleDeviceName_withSurrogates_corrected() {
        String original = "a" + TestUnicode.BAT_STRING + "b";
        String expected = "a??b";
        assertEquals(App.getCompatibleDeviceName(original), expected);
    }
}