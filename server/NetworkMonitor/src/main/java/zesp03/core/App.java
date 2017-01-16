package zesp03.core;

import zesp03.data.*;
import zesp03.entity.Controller;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;
import zesp03.util.Unicode;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
import java.util.stream.Collectors;

/**
 * Bardzo ważna klasa w naszym projekcie.
 * Wykonuje kluczowe operacje takie jak badanie sieci, pobieranie wyników, zarządzanie kontrolerami.
 * Wszystkie metody tej klasy są statyczne i thread-safe.
 * A przynajmniej wkrótce takie będą ;)
 */
public class App {
    private static final int CONTROLLER_NAME_MAX_CHARS = 85;
    private static final int DEVICE_NAME_MAX_CHARS = 85;
    private static final SNMPHandler snmp;

    static {
        try {
            snmp = new FakeSNMP();
        }
        catch(IOException exc) {
            throw new IllegalStateException(exc);
        }
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

    public static List<Dev> checkDevs() {
        final EntityManager em = Database.createEntityManager();
        final EntityTransaction tran = em.getTransaction();
        tran.begin();

        final List<Dev> list = em.createQuery(
                "SELECT c, d, s FROM DeviceSurvey s INNER JOIN s.device d INNER JOIN d.controller c WHERE s.id IN (" +
                        "SELECT MAX(id) FROM DeviceSurvey WHERE (device, timestamp) IN (" +
                        "SELECT device, MAX(timestamp) FROM DeviceSurvey GROUP BY device" +
                        ") GROUP BY id" +
                        ")",
                Object[].class)
                .getResultList()
                .stream()
                .map( arr -> new Dev( (Controller)arr[0], (Device)arr[1], (DeviceSurvey)arr[2] ) )
                .collect(Collectors.toList());

        tran.commit();
        em.close();
        return list;
    }

    //TODO użyj transakcji
    public static synchronized void examineAll() throws SQLException {
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

    //TODO użyj transakcji
    protected static synchronized void examineController(final int controllerId, final String ipv4) throws SQLException {
        HashMap<String, DeviceState> surveyed;
        try {
            surveyed = filterDevices( snmp.queryDevices(ipv4) );
        }
        catch(SNMPException exc) {
            log( "examineController", exc );
            return;
        }
        if( surveyed.size() < 1 )return;
        surveyed.forEach( (name, ds) -> ds.setId(-1) );

        Instant now = Instant.now();
        long longTS = now.getEpochSecond();
        if(longTS <= 0 || longTS > Integer.MAX_VALUE)
            throw new IllegalStateException("timestamp overflow");
        int timestamp = (int)longTS;

        try( Connection con = Database.connect() ) {
            try(Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT `name`, id FROM device") ) {
                while( res.next() ) {
                    String name = res.getString("name");
                    int id = res.getInt("id");
                    DeviceState ds = surveyed.get(name);
                    if(ds != null)ds.setId(id);
                }
            }

            final ArrayList<String> strangeNames = new ArrayList<>();
            surveyed.forEach( (deviceName, deviceState) -> {
                if( deviceState.getId() == -1 )strangeNames.add(deviceName);
            } );

            if( strangeNames.size() > 0 ) {
                final StringBuilder sb = new StringBuilder();
                sb.append("INSERT INTO device (`name`, is_known, controller_id) VALUES ");
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
                for (Map.Entry<String, DeviceState> entry : surveyed.entrySet()) {
                    final String deviceName = entry.getKey();
                    final DeviceState deviceState = entry.getValue();
                    prep.setInt(4 * x + 1, timestamp);
                    prep.setBoolean(4 * x + 2, deviceState.isEnabled());
                    prep.setInt(4 * x + 3, deviceState.getClientsSum());
                    prep.setInt(4 * x + 4, deviceState.getId());
                    x++;
                }
                prep.executeUpdate();
            }
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
    protected static HashMap<String, DeviceState> filterDevices(List<DeviceState> list) {
        final HashMap<String, DeviceState> result = new HashMap<>();
        for(DeviceState ds : list) {
            String name = ds.getName();
            if( ! isCompatibleDeviceName(name) ) {
                String old = name;
                name = getCompatibleDeviceName(name);
                log("filterDevices", "Device name \"" + old + "\" is not compatible.",
                        "Converted to \"" + name + "\".");
            }
            ds.setName(name);
            if( result.containsKey(name) ) {
                log("filterDevices", "Device name \"" + name + "\" occurs more than once.",
                        ds.toString() + " will be rejected.");
            }
            else {
                result.put(name, ds);
            }
        }
        return result;
    }

    //TODO zamień na coś lepszego
    protected static void log(String method, String message, String... extra) {
        DateTimeFormatter form = DateTimeFormatter.ofPattern("uuuu LLL dd, HH:mm:ss");
        String dt = LocalDateTime.now().format(form);
        System.out.println(dt + " App log (invoked by " + method + "):");
        System.out.println(message);
        for(String e : extra) {
            System.out.println(e);
        }
        System.out.println();
    }

    protected static void log(String method, Throwable exception, String... extra) {
        log(method, exception.toString(), extra);
    }
}
