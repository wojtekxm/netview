package zesp03.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.common.Database;
import zesp03.common.NotFoundException;
import zesp03.dto.AverageSurveyDto;
import zesp03.dto.MinmaxSurveyDto;
import zesp03.dto.OriginalSurveyDto;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;
import zesp03.entity.MinmaxSurvey;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<OriginalSurveyDto> getOriginalSurveys(long deviceId, int start, int end)
            throws NotFoundException {
        List<OriginalSurveyDto> result;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Device d = em.find(Device.class, deviceId);
            if (d == null)
                throw new NotFoundException("no such device");

            result = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE ds.device = :device AND ds.timestamp >= :start AND ds.timestamp < :end ORDER BY ds.timestamp ASC",
                    DeviceSurvey.class)
                    .setParameter("device", d)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList()
                    .stream()
                    .map(ds -> {
                        OriginalSurveyDto dto = new OriginalSurveyDto();
                        dto.setDeviceId(deviceId);
                        dto.setTime(ds.getTimestamp());
                        dto.setClientsSum(ds.getClientsSum());
                        return dto;
                    })
                    .collect(Collectors.toList());

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        return result;
    }

    /**
     * @param deviceId I urządzenia
     * @param start Timestamp w sekundach, początkowy czas do wyliczenia średniej (włącznie)
     * @param end Timestamp w sekundach, końcowy czas okresu do wyliczenia średniej (wyłącznie).
     *            Musi być większy od <code>start</code>.
     * @return Średnia liczba klientów dla danego urządzenia w wybranym okresie czasu.
     * @throws NotFoundException nie ma takiego urządzenia
     */
    public AverageSurveyDto getAverageSurvey(long deviceId, int start, int end)
            throws NotFoundException {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(end < 0)
            throw new IllegalArgumentException("end < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");

        final AverageSurveyDto result = new AverageSurveyDto();
        result.setDeviceId(deviceId);
        result.setTimeStart(start);
        result.setTimeEnd(end);
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
                result.setAvgClients(0.0);
                return result;
            }
            DeviceSurvey surEnd = listEnd.get(0);
            long cumulativeEnd = surEnd.getCumulative() + (long)surEnd.getClientsSum() * ( end - surEnd.getTimestamp() );
            log.debug("cumulativeEnd = {}", cumulativeEnd);

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
            log.debug("cumulativeStart = {}", cumulativeStart);

            long num = cumulativeEnd - cumulativeStart;
            result.setAvgClients( (double)num / (end - start) );

            tran.commit();
            return result;
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
    public MinmaxSurveyDto getMinmaxSimple(long deviceId, int start, int end)
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

            final MinmaxSurveyDto result = new MinmaxSurveyDto();
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

    /**
     * @param deviceId
     * @param start timestamp w sekundach,  inclusive
     * @param end timestamp w sekundach, exclusive
     * @return
     */
    public MinmaxSurveyDto getMinmaxComplex(long deviceId, int start, int end)
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

            final MinmaxSurveyDto result = new MinmaxSurveyDto();
            result.setDeviceId(deviceId);
            result.setTimeStart(start);
            result.setTimeEnd(end);
            log.debug("getMinmaxSurvey({}, {}, {})", deviceId, start, end);
            MinmaxPartial p = getFirstMinmax(deviceId, start, end, em);
            if(p == null) {
                log.debug("first minmax partial is null");
                p = getNextMinmax(deviceId, start, end, em);
            }
            if(p == null)
                throw new NotFoundException("no results for device with id=" + deviceId);
            log.debug("first partial min={} max={} last={} span={}",
                    p.min, p.max, p.lastSurvey, p.surveySpan);
            result.setMin( p.min );
            result.setMax( p.max );
            result.setSurveySpan( p.surveySpan );
            int after = p.lastSurvey;
            log.debug("first after={}", after);
            for(;;) {
                p = getNextMinmax(deviceId, after, end, em);
                if(p == null) {
                    log.debug("next partial is null");
                    break;
                }
                log.debug("next partial min={} max={} last={} span={}",
                        p.min, p.max, p.lastSurvey, p.surveySpan);
                result.setMin( Integer.min( result.getMin(), p.min ) );
                result.setMax( Integer.max( result.getMax(), p.max ) );
                result.setSurveySpan( result.getSurveySpan() + p.surveySpan );
                after = p.lastSurvey;
                log.debug("next after={}", after);
            }
            tran.commit();
            return result;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * @return null jak nie znajdzie
     */
    private MinmaxPartial getFirstMinmax(long deviceId, int start, int end, EntityManager em) {
        final List<MinmaxSurvey> lminmax = em.createQuery("SELECT ms FROM MinmaxSurvey ms WHERE " +
                        "ms.device.id = :did AND ms.firstSurvey <= :start AND ms.lastSurvey > :start AND " +
                        "ms.lastSurvey < :end ORDER BY ms.firstSurvey DESC, ms.surveySpan DESC",
                MinmaxSurvey.class)
                .setParameter("did", deviceId)
                .setParameter("start", start)
                .setParameter("end", end)
                .setMaxResults(1)
                .getResultList();
        if(lminmax.isEmpty()) {
            final List<DeviceSurvey> ldevice = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE " +
                            "ds.device.id = :did AND ds.timestamp <= :start ORDER BY ds.timestamp DESC",
                    DeviceSurvey.class)
                    .setParameter("did", deviceId)
                    .setParameter("start", start)
                    .setMaxResults(1)
                    .getResultList();
            if(ldevice.isEmpty())return null;
            final DeviceSurvey ds = ldevice.get(0);
            final MinmaxPartial p = new MinmaxPartial();
            p.min = ds.getClientsSum();
            p.max = ds.getClientsSum();
            p.surveySpan = 1;
            p.lastSurvey = ds.getTimestamp();
            return p;
        }
        final MinmaxSurvey ms = lminmax.get(0);
        final MinmaxPartial p = new MinmaxPartial();
        p.min = ms.getMin();
        p.max = ms.getMax();
        p.surveySpan = ms.getSurveySpan();
        p.lastSurvey = ms.getLastSurvey();
        return p;
    }

    private MinmaxPartial getNextMinmax(long deviceId, int after, int end, EntityManager em) {
        final List<MinmaxSurvey> lminmax = em.createQuery("SELECT ms FROM MinmaxSurvey ms WHERE " +
                        "ms.device.id = :did AND ms.firstSurvey > :after AND ms.lastSurvey < :end " +
                        "ORDER BY ms.firstSurvey ASC, ms.surveySpan DESC",
                MinmaxSurvey.class)
                .setParameter("did", deviceId)
                .setParameter("after", after)
                .setParameter("end", end)
                .setMaxResults(1)
                .getResultList();
        if(lminmax.isEmpty()) {
            final List<DeviceSurvey> ldevice = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE " +
                            "ds.device.id = :did AND ds.timestamp > :after AND ds.timestamp < :end ORDER BY ds.timestamp ASC",
                    DeviceSurvey.class)
                    .setParameter("did", deviceId)
                    .setParameter("after", after)
                    .setParameter("end", end)
                    .setMaxResults(1)
                    .getResultList();
            if(ldevice.isEmpty())return null;
            final DeviceSurvey ds = ldevice.get(0);
            final MinmaxPartial p = new MinmaxPartial();
            p.min = ds.getClientsSum();
            p.max = ds.getClientsSum();
            p.surveySpan = 1;
            p.lastSurvey = ds.getTimestamp();
            return p;
        }
        final MinmaxSurvey ms = lminmax.get(0);
        final MinmaxPartial p = new MinmaxPartial();
        p.min = ms.getMin();
        p.max = ms.getMax();
        p.surveySpan = ms.getSurveySpan();
        p.lastSurvey = ms.getLastSurvey();
        return p;
    }

    private static class MinmaxPartial {
        int min, max, surveySpan, lastSurvey;
    }
}
