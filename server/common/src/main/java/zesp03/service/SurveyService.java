package zesp03.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.common.Database;
import zesp03.data.DeviceNow;
import zesp03.data.MinmaxSurveyData;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;
import zesp03.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SurveyService {
    private static final Logger log = LoggerFactory.getLogger(SurveyService.class);

    /**
     * Zwraca listę badań dla urządzenia o id równym <code>device</code>.
     * Zwrócona lista będzie posortowana po czasie wykonania badania, rosnąco.
     * Lista może być pusta.
     * Lista będzie zawierać tylko badania których czas spełnia warunek: start <= czas < end.
     *
     * @param deviceId id urządzenia
     * @param start  timestamp w sekundach, dolny limit (włącznie) czasu badań
     * @param end    timestamp w sekundach, górny limit (wyłącznie) czasu badań
     */
    public List<DeviceSurvey> getOriginalSurveys(long deviceId, int start, int end) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Device d = em.find(Device.class, deviceId);
            if (d == null)
                throw new NotFoundException("no such device");

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

    /**
     * @param deviceId I urządzenia
     * @param start Timestamp w sekundach, początkowy czas do wyliczenia średniej (włącznie)
     * @param end Timestamp w sekundach, końcowy czas okresu do wyliczenia średniej (wyłącznie).
     *            Musi być większy od <code>start</code>.
     * @return Średnia liczba klientów dla danego urządzenia w wybranym okresie czasu.
     * @throws NotFoundException nie ma takiego urządzenia
     */
    public double getAverageSurvey(long deviceId, int start, int end)
            throws NotFoundException {
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

            Device device = em.find(Device.class, deviceId);
            if(device == null)
                throw new NotFoundException("no such device");

            List<DeviceSurvey> listEnd = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE ds.device = :device AND " +
                    "ds.timestamp <= :time ORDER BY ds.timestamp DESC ", DeviceSurvey.class)
                    .setParameter("device", device)
                    .setParameter("time", end)
                    .setMaxResults(1)
                    .getResultList();
            if(listEnd.isEmpty()) {
                tran.rollback();
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
                cumulativeStart = surStart.getCumulative() + (long)surStart.getClientsSum() * ( start - surStart.getTimestamp() );
            }

            tran.commit();
            double num = cumulativeEnd - cumulativeStart;
            return num / (end - start);
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * @param deviceId
     * @param start timestamp w sekundach,  inclusive
     * @param end timestamp w sekundach, exclusive
     * @return
     */
    public MinmaxSurveyData getMinmaxSimple(long deviceId, int start, int end)
            throws NotFoundException {
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
            tran.commit();
            return result;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

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
                "ds.device_id AS dsdid,\n" +
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
                "ds.device_id AS dsdid,\n" +
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
                "ds.device_id AS dsdid,\n" +
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