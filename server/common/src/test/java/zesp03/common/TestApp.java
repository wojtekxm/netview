package zesp03.common;

import org.junit.Test;
import zesp03.util.TestUnicode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

    @Test
    public void filterDevices_empty_empty() throws IOException {
        assertTrue(App.filterDevices(new ArrayList<>()).isEmpty());
    }

    @Test
    public void filterDevices_compatible_unchanged() throws IOException {
        SurveyInfo ds = new SurveyInfo();
        ds.setName("abc");
        ds.setEnabled(true);
        ds.setClientsSum(7);
        ds.setId(-1);
        final ArrayList<SurveyInfo> list = new ArrayList<>();
        list.add(ds);
        final HashMap<String, SurveyInfo> map = App.filterDevices(list);
        assertEquals("map size", map.size(), 1);
        map.forEach((key, survey) -> {
            assertEquals("name", ds.getName(), key);
            assertEquals("clients", ds.getClientsSum(), survey.getClientsSum());
            assertEquals("isEnabled", ds.isEnabled(), survey.isEnabled());
            assertEquals("id -1", survey.getId(), -1);
        });
    }

    @Test
    public void filterDevices_doubleNames_singleName() throws IOException {
        final SurveyInfo device1 = new SurveyInfo();
        device1.setName("x" + TestUnicode.BAT_STRING + "d");
        device1.setClientsSum(7);
        device1.setEnabled(false);
        final SurveyInfo device2 = new SurveyInfo();
        device2.setName(device1.getName());
        device2.setClientsSum(9);
        device2.setEnabled(true);
        final ArrayList<SurveyInfo> list = new ArrayList<>();
        list.add(device1);
        list.add(device2);
        final HashMap<String, SurveyInfo> map = App.filterDevices(list);
        assertEquals("map size", map.size(), 1);
        final SurveyInfo d = map.get("x??d");
        assertNotNull(d);
        assertEquals("id", d.getId(), 0);
        assertEquals("name", d.getName(), "x??d");
        assertEquals("isEnabled", d.isEnabled(), device1.isEnabled());
        assertEquals("clientsSum", d.getClientsSum(), device1.getClientsSum());
        assertNotEquals("isEnabled", d.isEnabled(), device2.isEnabled());
        assertNotEquals("clientsSum", d.getClientsSum(), device2.getClientsSum());
    }
}