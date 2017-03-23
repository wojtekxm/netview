package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zesp03.common.core.Database;
import zesp03.common.data.DeviceNow;
import zesp03.common.data.MinmaxSurveyData;
import zesp03.common.data.SurveyInfo;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceSurvey;
import zesp03.common.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService {
    private static final Logger log = LoggerFactory.getLogger(SurveyServiceImpl.class);
    private final NetworkService networkService;

    @Autowired
    public SurveyServiceImpl(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public List<DeviceSurvey> getOriginal(long deviceId, int start, int end) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(end < 0)
            throw new IllegalArgumentException("end < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Device d = em.find(Device.class, deviceId);
            if (d == null)
                throw new NotFoundException("device");

            List<DeviceSurvey>list = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE " +
                            "ds.device = :device AND ds.timestamp >= :start AND " +
                            "ds.timestamp < :end ORDER BY ds.timestamp ASC",
                    DeviceSurvey.class)
                    .setParameter("device", d)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList();

            tran.commit();
            return list;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public double getAverage(long deviceId, int start, int end) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(end < 0)
            throw new IllegalArgumentException("end < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            double avg = getAverage(deviceId, start, end, em);
            tran.commit();
            return avg;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    protected double getAverage(long deviceId, int start, int end, EntityManager em) {
        Device device = em.find(Device.class, deviceId);
        if(device == null)
            throw new NotFoundException("device");

        List<DeviceSurvey> listEnd = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE ds.device = :device AND " +
                "ds.timestamp <= :time ORDER BY ds.timestamp DESC ", DeviceSurvey.class)
                .setParameter("device", device)
                .setParameter("time", end)
                .setMaxResults(1)
                .getResultList();
        if(listEnd.isEmpty()) {
            return 0.0;
        }
        DeviceSurvey surEnd = listEnd.get(0);
        long cumulativeEnd = surEnd.getCumulative() + (long)surEnd.getClientsSum() * ( end - surEnd.getTimestamp() );

        List<DeviceSurvey> listStart = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE ds.device = :device AND " +
                "ds.timestamp <= :time ORDER BY ds.timestamp DESC ", DeviceSurvey.class)
                .setParameter("device", device)
                .setParameter("time", start)
                .setMaxResults(1)
                .getResultList();
        long cumulativeStart;
        if(listStart.isEmpty()) {
            cumulativeStart = 0;
        }
        else {
            DeviceSurvey surStart = listStart.get(0);
            cumulativeStart = surStart.getCumulative() + (long) surStart.getClientsSum() * (start - surStart.getTimestamp());
        }
        double num = cumulativeEnd - cumulativeStart;
        double avg = num / (end - start);
        if(avg < 0.0) {
            log.warn("avg < 0\n" +
                    "deviceId={} start={}, end={}" +
                    "cumulativeStart={} cumulativeEnd={} num={} avg={}",
                    deviceId, start, end, cumulativeStart, cumulativeEnd, num, avg);
            avg = 0.0;
        }
        return avg;
    }

    @Override
    public List<Double> getMultiAverage(long deviceId, int start, int groups, int groupTime) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(groups < 0)
            throw new IllegalArgumentException("groups < 0");
        if(groupTime < 1)
            throw new IllegalArgumentException("groupTime < 1");

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            int begin = start;
            List<Double> list = new ArrayList<>();
            for(int i = 0; i < groups; i++) {
                list.add( getAverage(deviceId, begin, begin + groupTime, em) );
                begin += groupTime;
            }
            tran.commit();
            return list;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public MinmaxSurveyData getMinmax(long deviceId, int start, int end) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(end < 0)
            throw new IllegalArgumentException("end < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            final MinmaxSurveyData result = getMinmax(deviceId, start, end, em);
            tran.commit();
            return result;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    protected MinmaxSurveyData getMinmax(long deviceId, int start, int end, EntityManager em) {
        final MinmaxSurveyData result = new MinmaxSurveyData();
        result.setDeviceId(deviceId);
        result.setTimeStart(start);
        result.setTimeEnd(end);
        List<Integer> begins = em.createQuery("SELECT ds.timestamp FROM DeviceSurvey ds WHERE " +
                        "ds.device.id = :did AND ds.timestamp <= :start ORDER BY ds.timestamp DESC",
                Integer.class)
                .setParameter("did", deviceId)
                .setParameter("start", start)
                .setMaxResults(1)
                .getResultList();
        int begin = start;
        if(!begins.isEmpty())begin = begins.get(0);
        Object[] mm = em.createQuery("SELECT MIN(ds.clientsSum), MAX(ds.clientsSum), " +
                "COUNT(ds) FROM DeviceSurvey ds WHERE ds.device.id = :did AND " +
                "ds.timestamp >= :begin AND ds.timestamp < :end", Object[].class)
                .setParameter("did", deviceId)
                .setParameter("begin", begin)
                .setParameter("end", end)
                .getSingleResult();
        Integer min = (Integer)mm[0];
        Integer max = (Integer)mm[1];
        Long span = (Long)mm[2];
        if(min == null || max == null || span == null)
            throw new NotFoundException("no results for device with id=" + deviceId);
        result.setMin(min);
        result.setMax(max);
        result.setSurveySpan((int)(long)span);
        return result;
    }

    @Override
    public List<MinmaxSurveyData> getMultiMinmax(long deviceId, int start, int groups, int groupTime) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(groups < 0)
            throw new IllegalArgumentException("groups < 0");
        if(groupTime < 1)
            throw new IllegalArgumentException("groupTime < 1");

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            int begin = start;
            List<MinmaxSurveyData> list = new ArrayList<>();
            for(int i = 0; i < groups; i++) {
                list.add( getMinmax(deviceId, begin, begin + groupTime, em) );
                begin += groupTime;
            }
            tran.commit();
            return list;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public List<DeviceNow> checkAll() {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List<DeviceNow> r = checkAll(em);
            tran.commit();
            return r;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public List<DeviceNow> checkSome(Collection<Long> ids) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List<DeviceNow> r = checkSome(ids, em);
            tran.commit();
            return r;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public DeviceNow checkOne(Long id) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            DeviceNow r = checkOne(id, em);
            tran.commit();
            return r;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public List<DeviceNow> checkAll(EntityManager em) {
        List list = em.createNativeQuery("SELECT device.id AS devid,\n" +
                "device.name AS devname,\n" +
                "device.description AS devdesc,\n" +
                "device.is_known AS devknown,\n" +
                "device.controller_id AS devcid,\n" +
                "ds.id AS dsid,\n" +
                "ds.`timestamp` AS dstime,\n" +
                "ds.is_enabled AS dsen,\n" +
                "ds.clients_sum AS dscli,\n" +
                "ds.cumulative AS dscum FROM\n" +
                "device LEFT JOIN (\n" +
                "SELECT ds.id, ds.`timestamp`, ds.is_enabled, ds.clients_sum, ds.device_id, ds.cumulative FROM\n" +
                "device_survey ds INNER JOIN (\n" +
                "SELECT device_id, MAX(`timestamp`) AS m FROM device_survey GROUP BY device_id\n" +
                ") best ON ds.device_id = best.device_id AND ds.timestamp = best.m\n" +
                ") ds ON device.id = ds.device_id")
                .getResultList();
        ArrayList<DeviceNow> result = new ArrayList<>();
        for(Object obj : list) {
            Object[] arr = (Object[])obj;
            result.add( fromRow(arr) );
        }
        return result;
    }

    @Override
    public List<DeviceNow> checkSome(Collection<Long> ids, EntityManager em) {
        List list = em.createNativeQuery("SELECT device.id AS devid,\n" +
                "device.name AS devname,\n" +
                "device.description AS devdesc,\n" +
                "device.is_known AS devknown,\n" +
                "device.controller_id AS devcid,\n" +
                "ds.id AS dsid,\n" +
                "ds.`timestamp` AS dstime,\n" +
                "ds.is_enabled AS dsen,\n" +
                "ds.clients_sum AS dscli,\n" +
                "ds.cumulative AS dscum FROM\n" +
                "device LEFT JOIN (\n" +
                "SELECT ds.id, ds.`timestamp`, ds.is_enabled, ds.clients_sum, ds.device_id, ds.cumulative FROM\n" +
                "device_survey ds INNER JOIN (\n" +
                "SELECT device_id, MAX(`timestamp`) AS m FROM device_survey GROUP BY device_id\n" +
                ") best ON ds.device_id = best.device_id AND ds.timestamp = best.m\n" +
                ") ds ON device.id = ds.device_id WHERE device.id IN (:ids)")
                .setParameter("ids", ids)
                .getResultList();
        ArrayList<DeviceNow> result = new ArrayList<>();
        for(Object obj : list) {
            Object[] arr = (Object[])obj;
            result.add( fromRow(arr) );
        }
        return result;
    }

    @Override
    public DeviceNow checkOne(Long id, EntityManager em) {
        List list = em.createNativeQuery("SELECT device.id AS devid,\n" +
                "device.name AS devname,\n" +
                "device.description AS devdesc,\n" +
                "device.is_known AS devknown,\n" +
                "device.controller_id AS devcid,\n" +
                "ds.id AS dsid,\n" +
                "ds.`timestamp` AS dstime,\n" +
                "ds.is_enabled AS dsen,\n" +
                "ds.clients_sum AS dscli,\n" +
                "ds.cumulative AS dscum FROM\n" +
                "device LEFT JOIN (\n" +
                "SELECT ds.id, ds.`timestamp`, ds.is_enabled, ds.clients_sum, ds.device_id, ds.cumulative FROM\n" +
                "device_survey ds INNER JOIN (\n" +
                "SELECT device_id, MAX(`timestamp`) AS m FROM device_survey GROUP BY device_id\n" +
                ") best ON ds.device_id = best.device_id AND ds.timestamp = best.m\n" +
                ") ds ON device.id = ds.device_id WHERE device.id = :id")
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList();
        if(list.isEmpty())
            throw new NotFoundException("device");
        return fromRow( (Object[])list.get(0) );
    }

    @Override
    public int examineAll() {
        log.info("network survey begins");
        List<Long> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT c.id FROM Controller c", Long.class)
                    .getResultList();

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        int result = 0;
        for (Long id : list) {
            try {
                result += examineOne(id);
            } catch (RuntimeException exc) {
                log.error("failed to examine controller", exc);
            }
        }
        return result;
    }

    @Override
    public int examineOne(long controllerId) {
        final Instant start = Instant.now();
        final int timestamp = (int) start.getEpochSecond();
        String ipv4;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller c = em.find(Controller.class, controllerId);
            if(c == null)
                throw new NotFoundException("controller");
            ipv4 = c.getIpv4();

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        final HashSet<String> deviceNames = new HashSet<>();
        final HashMap<String, SurveyInfo> name2info = new HashMap<>();
        for(SurveyInfo info : networkService.queryDevices(ipv4)) {
            deviceNames.add(info.getName());
            name2info.put(info.getName(), info);
        }
        if(name2info.isEmpty()) {
            log.info("survey of controller (id={}, ip={}) returned no devices", controllerId, ipv4);
            return 0;
        }

        em = null;
        tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller controller = em.find(Controller.class, controllerId);
            if (controller == null)
                throw new NotFoundException("controller");

            final HashMap<String, Device> name2device = makeDevices(deviceNames, controller, em);
            em.flush();
            final HashMap<Long, DeviceNow> devid2now = new HashMap<>();
            checkSome(
                    name2device
                            .entrySet()
                            .stream()
                            .map( e -> e.getValue().getId() )
                            .collect(Collectors.toSet()),
                    em
            ).forEach( di -> devid2now.put(di.getId(), di) );
            final List<DeviceSurvey> ds2persist = new ArrayList<>();
            for(String name : deviceNames) {
                final SurveyInfo info = name2info.get(name);
                final Device device = name2device.get(name);
                final DeviceSurvey sur = new DeviceSurvey();
                sur.setTimestamp(timestamp);
                sur.setEnabled(info.isEnabled());
                sur.setClientsSum(info.getClientsSum());
                sur.setDevice(device);
                final DeviceNow before = devid2now.get(device.getId());
                if(before == null || before.getSurvey() == null) {
                    sur.setCumulative(0L);
                    ds2persist.add(sur);
                }
                else {
                    DeviceSurvey z = before.getSurvey();
                    if(! z.getTimestamp().equals(sur.getTimestamp()) ) {
                        sur.setCumulative( z.getCumulative() + z.getClientsSum() *
                                ( sur.getTimestamp() - z.getTimestamp() ) );
                        ds2persist.add(sur);
                    }
                }
            }
            for(DeviceSurvey ds : ds2persist) {
                em.persist(ds);
            }

            tran.commit();
            return ds2persist.size();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * Dla każdej nazwy z <code>list</code> wstawia do bazy nowe urządzenie jeśli takie nie istnieje.
     * @return mapa [nazwa urządzenia => urządzenie z bazy]
     */
    private HashMap<String, Device> makeDevices(Set<String> deviceNames, Controller controller, EntityManager em) {
        if(deviceNames.isEmpty())return new HashMap<>();
        final HashMap<String, Device> existing = new HashMap<>();
        for(String name : deviceNames) {
            existing.put(name, null);
        }
        em.createQuery("SELECT d FROM Device d WHERE d.name IN (:names)", Device.class)
                .setParameter("names", deviceNames)
                .getResultList()
                .forEach( device -> existing.put(device.getName(), device) );
        final HashMap<String, Device> result = new HashMap<>();
        existing.forEach( (name, device) -> {
            if(device == null) {
                Device d = new Device();
                d.setName(name);
                d.setController(controller);
                d.setKnown(false);
                d.setDescription(null);
                em.persist(d);
                result.put(name, d);
            }
            else result.put(name, device);
        } );
        return result;
    }

    private static DeviceNow fromRow(Object[] row) {
        final DeviceNow d = new DeviceNow();
        d.setId( getLongOrNull(row[0]) );
        d.setName( (String)row[1] );
        d.setDescription( (String)row[2] );
        d.setKnown( (Boolean)row[3] );
        d.setControllerId( getLongOrNull(row[4]) );
        final Long sid = getLongOrNull(row[5]);
        if(sid != null) {
            final DeviceSurvey s = new DeviceSurvey();
            s.setId( sid );
            s.setTimestamp( getIntegerOrNull(row[6]) );
            s.setEnabled( (Boolean)row[7] );
            s.setClientsSum( getIntegerOrNull(row[8]) );
            s.setCumulative( getLongOrNull(row[9]) );
            d.setSurvey(s);
        }
        else d.setSurvey(null);
        return d;
    }

    private static Long getLongOrNull(Object o) {
        if(o == null)return null;
        else return ((Number)o).longValue();
    }

    private static Integer getIntegerOrNull(Object o) {
        if(o == null)return null;
        else return ((Number)o).intValue();
    }
}