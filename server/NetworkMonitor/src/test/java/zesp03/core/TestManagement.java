package zesp03.core;

import static org.junit.Assert.*;

import org.junit.Test;
import zesp03.data.DeviceState;
import zesp03.util.TestUnicode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestManagement {
    @Test
    public void isValidControllerName_empty_false() {
        assertFalse( App.isValidControllerName("") );
    }

    @Test
    public void isValidControllerName_tooLong_false() {
        String s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb" +
                "cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc";
        assertFalse( App.isValidControllerName(s) );
    }

    @Test
    public void isValidControllerName_wifi_true() {
        assertTrue( App.isValidControllerName("wifi") );
    }

    @Test
    public void isValidControllerName_withSupplementary_false() {
        String s = "wifi" + TestUnicode.BAT_STRING + "1234";
        assertFalse( App.isValidControllerName(s) );
    }

    @Test
    public void isCompatibleDeviceName_abc_true() {
        assertTrue( App.isCompatibleDeviceName("abc") );
    }

    @Test
    public void isCompatibleDeviceName_empty_true() {
        assertTrue( App.isCompatibleDeviceName("") );
    }

    @Test
    public void isCompatibleDeviceName_tooLong_false() {
        String s = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" +
                "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy" +
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        assertFalse( App.isCompatibleDeviceName(s) );
    }

    @Test
    public void isCompatibleDeviceName_withSurrogates_false() {
        String s = "1" + TestUnicode.BAT_STRING + "2";
        assertFalse( App.isCompatibleDeviceName(s) );
    }

    @Test
    public void getCompatibleDeviceName_valid_theSame() {
        assertEquals( App.getCompatibleDeviceName("123"), "123" );
    }

    @Test
    public void getCompatibleDeviceName_withSurrogates_corrected() {
        String original = "a" + TestUnicode.BAT_STRING + "b";
        String expected = "a??b";
        assertEquals( App.getCompatibleDeviceName(original), expected );
    }

    @Test
    public void filterDevices_empty_empty() throws IOException {
        assertTrue( App.filterDevices( new ArrayList<>() ).isEmpty() );
    }

    @Test
    public void filterDevices_compatible_unchanged() throws IOException {
        DeviceState ds = new DeviceState();
        ds.setName("abc");
        ds.setEnabled(true);
        ds.setClientsSum(7);
        ds.setId(-1);
        final ArrayList<DeviceState> list = new ArrayList<>();
        list.add( ds );
        final HashMap<String, DeviceState> map = App.filterDevices(list);
        assertEquals("map size", map.size(), 1);
        map.forEach( (key, survey) -> {
            assertEquals( "name", ds.getName(), key );
            assertEquals( "clients", ds.getClientsSum(), survey.getClientsSum() );
            assertEquals( "isEnabled", ds.isEnabled(), survey.isEnabled() );
            assertEquals( "id -1", survey.getId(), -1 );
        } );
    }

    @Test
    public void filterDevices_doubleNames_singleName() throws IOException {
        DeviceState d1 = new DeviceState();
        d1.setName("x" + TestUnicode.BAT_STRING + "d");
        d1.setClientsSum(7);
        d1.setEnabled(false);
        DeviceState d2 = new DeviceState();
        d2.setName( d1.getName() );
        d2.setClientsSum(9);
        d2.setEnabled(true);
        final ArrayList<DeviceState> list = new ArrayList<>();
        list.add(d1);
        list.add(d2);
        final HashMap<String, DeviceState> map = App.filterDevices(list);
        assertEquals("map size", map.size(), 1);
        DeviceState ds = map.get( "x??d" );
        assertNotNull( ds );
        assertEquals( "isEnabled", ds.isEnabled(), d1.isEnabled() );
        assertEquals( "clientsSum", ds.getClientsSum(), d1.getClientsSum() );
        assertEquals( "id -1", ds.getId(), -1 );
        assertNotEquals( "isEnabled", ds.isEnabled(), d2.isEnabled() );
        assertNotEquals( "clientsSum", ds.getClientsSum(), d2.getClientsSum() );
    }
}