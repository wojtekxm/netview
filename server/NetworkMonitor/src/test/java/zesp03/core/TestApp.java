package zesp03.core;

import static org.junit.Assert.*;

import org.junit.Test;
import zesp03.data.DeviceState;
import zesp03.util.TestUnicode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestApp {
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
        final DeviceState device1 = new DeviceState();
        device1.setName("x" + TestUnicode.BAT_STRING + "d");
        device1.setClientsSum(7);
        device1.setEnabled(false);
        final DeviceState device2 = new DeviceState();
        device2.setName( device1.getName() );
        device2.setClientsSum(9);
        device2.setEnabled(true);
        final ArrayList<DeviceState> list = new ArrayList<>();
        list.add(device1);
        list.add(device2);
        final HashMap<String, DeviceState> map = App.filterDevices(list);
        assertEquals("map size", map.size(), 1);
        final DeviceState d = map.get( "x??d" );
        assertNotNull( d );
        assertEquals( "id", d.getId(), 0 );
        assertEquals( "name", d.getName(), "x??d" );
        assertEquals( "isEnabled", d.isEnabled(), device1.isEnabled() );
        assertEquals( "clientsSum", d.getClientsSum(), device1.getClientsSum() );
        assertNotEquals( "isEnabled", d.isEnabled(), device2.isEnabled() );
        assertNotEquals( "clientsSum", d.getClientsSum(), device2.getClientsSum() );
    }
}