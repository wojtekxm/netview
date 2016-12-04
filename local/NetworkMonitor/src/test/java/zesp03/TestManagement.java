package zesp03;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestManagement {
    @Test
    public void isValidControllerName_empty_false() {
        assertFalse( Management.isValidControllerName("") );
    }

    @Test
    public void isValidControllerName_tooLong_false() {
        String s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb" +
                "cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc";
        assertFalse( Management.isValidControllerName(s) );
    }

    @Test
    public void isValidControllerName_wifi_true() {
        assertTrue( Management.isValidControllerName("wifi") );
    }

    @Test
    public void isValidControllerName_withSupplementary_false() {
        String s = "wifi" + TestUnicode.BAT_STRING + "1234";
        assertFalse( Management.isValidControllerName(s) );
    }

    @Test
    public void isCompatibleDeviceName_abc_true() {
        assertTrue( Management.isCompatibleDeviceName("abc") );
    }

    @Test
    public void isCompatibleDeviceName_empty_true() {
        assertTrue( Management.isCompatibleDeviceName("") );
    }

    @Test
    public void isCompatibleDeviceName_tooLong_false() {
        String s = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" +
                "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy" +
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        assertFalse( Management.isCompatibleDeviceName(s) );
    }

    @Test
    public void isCompatibleDeviceName_withSurrogates_false() {
        String s = "1" + TestUnicode.BAT_STRING + "2";
        assertFalse( Management.isCompatibleDeviceName(s) );
    }

    @Test
    public void getCompatibleDeviceName_valid_theSame() {
        assertEquals( Management.getCompatibleDeviceName("123"), "123" );
    }

    @Test
    public void getCompatibleDeviceName_withSurrogates_corrected() {
        String original = "a" + TestUnicode.BAT_STRING + "b";
        String expected = "a??b";
        assertEquals( Management.getCompatibleDeviceName(original), expected );
    }
}