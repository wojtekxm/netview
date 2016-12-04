package zesp03;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Management {
    private static final int CONTROLLER_NAME_MAX_CHARS = 85;
    private static final int DEVICE_NAME_MAX_CHARS = 85;
    private final SNMPHandler snmp;

    public Management() throws IOException {
        snmp = new FakeSNMP();
    }

    public static boolean isValidControllerName(String name) {
        if(name == null)return false;
        if(name.length() < 1)return false;
        if(name.length() > CONTROLLER_NAME_MAX_CHARS)return false;
        return Unicode.noSurrogates(name);
    }

    public static boolean isCompatibleDeviceName(String name) {
        return name.length() <= DEVICE_NAME_MAX_CHARS && Unicode.noSurrogates(name);
    }

    public static String getCompatibleDeviceName(String original) {
        int len = original.length();
        if(DEVICE_NAME_MAX_CHARS < len)len = DEVICE_NAME_MAX_CHARS;
        final StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++) {
            final char c = original.charAt(i);
            if( Character.isSurrogate(c) )sb.append('?');
            else sb.append(c);
        }
        return sb.toString().intern();
    }

    //TODO: wyrzucić Admin exception jeśli taki controller już istnieje (zamiast SQLException) [?]
    //TODO: wyrzucić Admin exception jeśli IP jest w złym formacie
    //TODO: użyć transakcji
    public void registerController(String name, String ipv4) throws SQLException, AdminException {
        if( ! isValidControllerName(name) )
            throw new AdminException("invalid controller name");
        String sql = "INSERT INTO controller (name, ipv4) VALUES (?, ?)";
        try(Connection c = Database.connect();
            PreparedStatement ps = c.prepareStatement(sql) ) {
            ps.setString(1, name);
            ps.setString(2, ipv4);
            ps.executeUpdate();
        }
    }

    public void registerNewDevice(String name, int controllerId) throws SQLException, AdminException {
        if( ! isCompatibleDeviceName(name) )
            throw new AdminException("invalid device name");
        String sql = "INSERT INTO device (name, is_known, description, controller_id) VALUES (?, TRUE, NULL, ?)";
        try(Connection c = Database.connect();
            PreparedStatement ps = c.prepareStatement(sql) ) {
            ps.setString(1, name);
            ps.setInt(2, controllerId);
            ps.executeUpdate();
        }
    }

    public void examineAll() throws SQLException, SNMPException {
        String sql = "SELECT id, ipv4 FROM controller";
        try(Connection con = Database.connect();
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(sql) ) {
            while( res.next() ) {
                int id = res.getInt("id");
                String ipv4 = res.getString("ipv4");
                examineController(id, ipv4, con);
            }
        }
    }

    private void examineController(int controllerId, String ipv4, Connection con) throws SQLException, SNMPException {
        final List<DeviceInfo> list = snmp.queryDevices(ipv4);
        makeCompatibleNames(list);
        makeUniqueNames(list);
        Instant now = Instant.now();
        long longTS = now.getEpochSecond();
        if(longTS <= 0 || longTS > Integer.MAX_VALUE)
            throw new IllegalStateException("timestamp overflow");
        int timestamp = (int)longTS;
        for(DeviceInfo info : list) {
            String sql1 = "SELECT id FROM device WHERE name = ?";
            int deviceId = -1;
            try( PreparedStatement prep = con.prepareStatement(sql1) ) {
                prep.setString(1, info.getName() );
                try( ResultSet res = prep.executeQuery() ) {
                    if( res.next() ) {
                        deviceId = res.getInt("id");
                    }
                }
            }
            if(deviceId == -1) {
                String sql2 = "INSERT INTO device (name, is_known, description, controller_id) VALUES (?, FALSE, NULL, ?)";
                try( PreparedStatement prep = con.prepareStatement(sql2, PreparedStatement.RETURN_GENERATED_KEYS) ) {
                    prep.setString( 1, info.getName() );
                    prep.setInt( 2, controllerId );
                    prep.executeUpdate();
                    try( ResultSet res = prep.getGeneratedKeys() ) {
                        res.next();
                        deviceId = res.getInt(1);
                    }
                }
            }
            String sql3 = "INSERT INTO device_survey (`timestamp`, is_enabled, clients_sum, device_id) VALUES (?, ?, ?, ?)";
            try(PreparedStatement prep = con.prepareStatement(sql3)) {
                prep.setInt(1, timestamp);
                prep.setBoolean(2, info.isEnabled());
                prep.setInt(3, info.getClients());
                prep.setInt(4, deviceId);
                prep.executeUpdate();
            }
        }
    }

    protected void makeCompatibleNames(List<DeviceInfo> list) {
        for(DeviceInfo info : list) {
            if( ! isCompatibleDeviceName( info.getName() ) ) {
                info.setName(getCompatibleDeviceName(info.getName()));
                log("makeCompatibleNames", "Name \"" + info.getName() + "\" is not compatible.");
            }
        }
    }

    protected void makeUniqueNames(List<DeviceInfo> list) {
        final HashMap<String, DeviceInfo> map = new HashMap<>();
        Iterator<DeviceInfo> iter = list.iterator();
        while( iter.hasNext() ) {
            DeviceInfo info = iter.next();
            DeviceInfo original = map.get( info.getName() );
            if(original != null) {
                iter.remove();
                log("makeUniqueNames", "Name \"" + info.getName() + "\" occurs more than once. " +
                        original.toString() + " kept " + info.toString() + " rejected.");
            }
            else map.put(info.getName(), info);
        }
    }

    private void log(String method, String message) {
        // !!! zamień na coś lepszego
        System.out.println("Management log [" + method + "]:");
        System.out.println(message);
        System.out.println();
    }
}
