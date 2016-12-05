package zesp03;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Management {
    private static final int CONTROLLER_NAME_MAX_CHARS = 85;
    private static final int DEVICE_NAME_MAX_CHARS = 85;
    private final SNMPHandler snmp;

    public Management() throws IOException {
        snmp = new FakeSNMP();
    }

    public static boolean isValidControllerName(String name) {
        if( name.length() < 1 ) return false;
        if( name.length() > CONTROLLER_NAME_MAX_CHARS )return false;
        return Unicode.noSurrogates(name);
    }

    public static boolean isCompatibleDeviceName(String name) {
        if( name.length() > DEVICE_NAME_MAX_CHARS )return false;
        return Unicode.noSurrogates(name);
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
        final HashMap<Integer, String> map = new HashMap<>();
        String sql = "SELECT id, ipv4 FROM controller";
        try(Connection con = Database.connect();
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(sql) ) {
            while( res.next() ) {
                int id = res.getInt("id");
                String ipv4 = res.getString("ipv4");
                map.put(id, ipv4);
            }
        }
        map.forEach( (controllerId, ipv4) -> {
            // czy ten controllerId nadal będzie istnieć w bazie po zakończeniu powyższego Connection [?]
            try {
                examineController(controllerId, ipv4);
            }
            catch(SQLException exc) {
                log( "examineAll", exc );
            }
        } );
    }

    protected void examineController(int controllerId, String ipv4) throws SQLException {
        HashMap<String, SurveyItem> surveyed;
        try {
            surveyed = filterDevices( snmp.queryDevices(ipv4) );
        }
        catch(SNMPException exc) {
            log( "examineController", exc );
            return;
        }
        if( surveyed.size() < 1 )return;

        Instant now = Instant.now();
        long longTS = now.getEpochSecond();
        if(longTS <= 0 || longTS > Integer.MAX_VALUE)
            throw new IllegalStateException("timestamp overflow");
        int timestamp = (int)longTS;

        try( Connection con = Database.connect() ) {
            try(Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT name, id FROM device") ) {
                while( res.next() ) {
                    String name = res.getString("name");
                    int id = res.getInt("id");
                    SurveyItem item = surveyed.get(name);
                    if(item != null)item.setId(id);
                }
            }

            final ArrayList<String> strangeNames = new ArrayList<>();
            surveyed.forEach( (deviceName, surveyItem) -> {
                if( surveyItem.getId() == -1 )strangeNames.add(deviceName);
            } );

            if( strangeNames.size() > 0 ) {
                final StringBuilder sb = new StringBuilder();
                sb.append("INSERT INTO device (name, is_known, controller_id) VALUES ");
                for(int i = 0; i < strangeNames.size(); i++) {
                    if(i > 0)sb.append(", ");
                    sb.append("(?, FALSE, ?)");
                }
                String sql = sb.toString().intern();
                try( PreparedStatement prep = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS) ) {
                    for(int i = 0; i < strangeNames.size(); i++) {
                        prep.setString( 2 * i + 1, strangeNames.get(i) );
                        prep.setInt( 2 * i + 2, controllerId );
                    }
                    prep.executeUpdate();
                    try( ResultSet res = prep.getGeneratedKeys() ) {
                        for (String name : strangeNames) {
                            res.next();
                            int deviceId = res.getInt(1);
                            surveyed.get(name).setId(deviceId);
                        }
                    }
                }
            }

            final StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO device_survey (`timestamp`, is_enabled, clients_sum, device_id) VALUES ");
            for(int i = 0; i < surveyed.size(); i++) {
                if(i > 0)sb.append(", ");
                sb.append("(?, ?, ?, ?)");
            }
            String sql = sb.toString().intern();
            try( PreparedStatement prep = con.prepareStatement(sql) ) {
                int x = 0;
                for (Map.Entry<String, SurveyItem> entry : surveyed.entrySet()) {
                    final String deviceName = entry.getKey();
                    final SurveyItem surveyItem = entry.getValue();
                    prep.setInt(4 * x + 1, timestamp);
                    prep.setBoolean(4 * x + 2, surveyItem.isEnabled());
                    prep.setInt(4 * x + 3, surveyItem.getClients());
                    prep.setInt(4 * x + 4, surveyItem.getId());
                    x++;
                }
                prep.executeUpdate();
            }
        }
    }

    protected static class SurveyItem {
        private int id;
        private final int clients;
        private final boolean enabled;

        SurveyItem(int clients, boolean isEnabled) {
            this.id = -1;
            this.clients = clients;
            this.enabled = isEnabled;
        }

        public int getId() {
            return id;
        }

        public int getClients() {
            return clients;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    /**
     * Transformuje listę urządzeń w mapę gdzie kluczem jest nazwa urządzenia.
     * Nazwy urządzeń w mapie będą przekształcone do kompatybilnej postaci za pomocą getCompatibleDeviceName.
     * W przypadku wielokrotnego wystąpienia nazwy w liscie, w mapie zostanie wpisany pierwszy element listy z daną nazwą.
     * Id każdego urządzenia w mapie będzie mieć wartość domyślną czyli -1.
     * @param list lista informacji o urządzeniach zwrócona przez SNMPHandler.
     *             Lista nie będzie modyfikowana przez tą metodę.
     * @return mapa utworzona na podstawie przefiltrowanej listy urządzeń
     */
    protected HashMap<String, SurveyItem> filterDevices(List<DeviceInfo> list) {
        final HashMap<String, SurveyItem> result = new HashMap<>();
        for(DeviceInfo info : list) {
            String name = info.getName();
            if( ! isCompatibleDeviceName(name) ) {
                String old = name;
                name = getCompatibleDeviceName(name);
                log("filterDevices", "Device name \"" + old + "\" is not compatible.",
                        "Converted to \"" + name + "\".");
            }
            if( result.containsKey(name) ) {
                log("filterDevices", "Device name \"" + name + "\" occurs more than once.",
                        info.toString() + " will be rejected.");
            }
            else {
                SurveyItem s = new SurveyItem( info.getClients(), info.isEnabled() );
                result.put(name, s);
            }
        }
        return result;
    }

    protected void log(String method, String message, String... extra) {
        // !!! zamień na coś lepszego
        DateTimeFormatter form = DateTimeFormatter.ofPattern("uuuu LLL dd, HH:mm:ss");
        String dt = LocalDateTime.now().format(form);
        System.out.println(dt + " Management log (invoked by " + method + "):");
        System.out.println(message);
        for(String e : extra) {
            System.out.println(e);
        }
        System.out.println();
    }

    protected void log(String method, Throwable exception, String... extra) {
        log(method, exception.toString(), extra);
    }
}
