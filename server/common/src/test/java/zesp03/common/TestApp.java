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
    public void isValidUserName_empty_false() {
        assertFalse(App.isValidUserName(""));
    }

    @Test
    public void isValidUserName_admin_true() {
        assertTrue(App.isValidUserName("admin"));
    }

    @Test
    public void isValidUserName_colon_false() {
        assertFalse(App.isValidUserName(":"));
    }

    @Test
    public void isValidUserName_tooLong_false() {
        assertFalse(App.isValidUserName(TOO_LONG));
    }

    @Test
    public void isValidUserName_numbers_true() {
        assertTrue(App.isValidUserName("213475893425"));
    }

    @Test
    public void isValidControllerName_empty_false() {
        assertFalse(App.isValidControllerName(""));
    }

    @Test
    public void isValidControllerName_tooLong_false() {
        assertFalse(App.isValidControllerName(TOO_LONG));
    }

    @Test
    public void isValidControllerName_wifi_true() {
        assertTrue(App.isValidControllerName("wifi"));
    }

    @Test
    public void isValidControllerName_withSupplementary_false() {
        String s = "wifi" + TestUnicode.BAT_STRING + "1234";
        assertFalse(App.isValidControllerName(s));
    }

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