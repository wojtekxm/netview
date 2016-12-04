package zesp03;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Test
    public void filterDevices_empty_empty() throws IOException {
        Management mgm = new Management();
        assertTrue( mgm.filterDevices( new ArrayList<>() ).isEmpty() );
    }

    @Test
    public void filterDevices_compatible_unchanged() throws IOException {
        final Management mgm = new Management();
        DeviceInfo d = new DeviceInfo();
        d.setName("abc");
        d.setEnabled(true);
        d.setClients(7);
        final ArrayList<DeviceInfo> list = new ArrayList<>();
        list.add( d );
        final HashMap<String, Management.SurveyItem> map = mgm.filterDevices(list);
        assertEquals("map size", map.size(), 1);
        map.forEach( (key, survey) -> {
            assertEquals( "name", d.getName(), key );
            assertEquals( "clients", d.getClients(), survey.getClients() );
            assertEquals( "isEnabled", d.isEnabled(), survey.isEnabled() );
            assertEquals( "id -1", survey.getId(), -1 );
        } );
    }

    @Test
    public void filterDevices_doubleNames_singleName() throws IOException {
        Management mgm = new Management();
        DeviceInfo d1 = new DeviceInfo();
        d1.setName("x" + TestUnicode.BAT_STRING + "d");
        d1.setClients(7);
        d1.setEnabled(false);
        DeviceInfo d2 = new DeviceInfo();
        d2.setName( d1.getName() );
        d2.setClients(9);
        d2.setEnabled(true);
        final ArrayList<DeviceInfo> list = new ArrayList<>();
        list.add(d1);
        list.add(d2);
        final HashMap<String, Management.SurveyItem> map = mgm.filterDevices(list);
        assertEquals("map size", map.size(), 1);
        Management.SurveyItem si = map.get( "x??d" );
        assertNotNull( si );
        assertEquals( "isEnabled", si.isEnabled(), d1.isEnabled() );
        assertEquals( "clients", si.getClients(), d1.getClients() );
        assertEquals( "id -1", si.getId(), -1 );
        assertNotEquals( "isEnabled", si.isEnabled(), d2.isEnabled() );
        assertNotEquals( "clients", si.getClients(), d2.getClients() );
    }
}