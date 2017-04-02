package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zesp03.common.core.Database;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.MinmaxSurveyData;
import zesp03.common.data.SurveyInfo;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceFrequency;
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
    public List<DeviceSurvey> getOriginal(long deviceId, int frequencyMhz, int start, int end) {
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

            final DeviceFrequency df = getDeviceFrequency(deviceId, frequencyMhz, em);

            List<DeviceSurvey> list = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE " +
                            "ds.frequency = :df AND ds.timestamp >= :start AND " +
                            "ds.timestamp < :end ORDER BY ds.timestamp ASC",
                    DeviceSurvey.class)
                    .setParameter("df", df)
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

    protected DeviceFrequency getDeviceFrequency(long deviceId, int frequencyMhz, EntityManager em) {
        List<DeviceFrequency> freqList = em.createQuery("SELECT df FROM DeviceFrequency df WHERE " +
                "df.frequency = :f AND df.device.id = :d", DeviceFrequency.class)
                .setParameter("f", frequencyMhz)
                .setParameter("d", deviceId)
                .setMaxResults(1)
                .getResultList();
        if(freqList.isEmpty()) {
            throw new NotFoundException("device with this frequency");
        }
        return freqList.get(0);
    }

    @Override
    public double getAverage(long deviceId, int frequencyMhz, int start, int end) {
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

            double avg = getAverage(deviceId, frequencyMhz, start, end, em);
            tran.commit();
            return avg;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    protected double getAverage(long deviceId, int frequencyMhz, int start, int end, EntityManager em) {
        final DeviceFrequency df = getDeviceFrequency(deviceId, frequencyMhz, em);

        List<DeviceSurvey> listEnd = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE " +
                "ds.frequency = :df AND ds.timestamp <= :time " +
                "ORDER BY ds.timestamp DESC ", DeviceSurvey.class)
                .setParameter("df", df)
                .setParameter("time", end)
                .setMaxResults(1)
                .getResultList();
        if(listEnd.isEmpty()) {
            return 0.0;
        }
        DeviceSurvey surEnd = listEnd.get(0);
        long cumulativeEnd = surEnd.getCumulative() + (long)surEnd.getClientsSum() * ( end - surEnd.getTimestamp() );

        List<DeviceSurvey> listStart = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE " +
                "ds.frequency = :df AND ds.timestamp <= :time " +
                "ORDER BY ds.timestamp DESC ", DeviceSurvey.class)
                .setParameter("df", df)
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
    public List<Double> getMultiAverage(long deviceId, int frequencyMhz, int start, int groups, int groupTime) {
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
                list.add( getAverage(deviceId, frequencyMhz, begin, begin + groupTime, em) );
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
    public MinmaxSurveyData getMinmax(long deviceId, int frequencyMhz, int start, int end) {
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

            final MinmaxSurveyData result = getMinmax(deviceId, frequencyMhz, start, end, em);
            tran.commit();
            return result;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    protected MinmaxSurveyData getMinmax(long deviceId, int frequencyMhz, int start, int end, EntityManager em) {
        final MinmaxSurveyData result = new MinmaxSurveyData();
        result.setDeviceId(deviceId);
        result.setTimeStart(start);
        result.setTimeEnd(end);

        final DeviceFrequency df = getDeviceFrequency(deviceId, frequencyMhz, em);
        List<Integer> begins = em.createQuery("SELECT ds.timestamp FROM DeviceSurvey ds WHERE " +
                        "ds.frequency = :df AND ds.timestamp <= :start ORDER BY ds.timestamp DESC",
                Integer.class)
                .setParameter("df", df)
                .setParameter("start", start)
                .setMaxResults(1)
                .getResultList();
        int begin = start;
        if(!begins.isEmpty())begin = begins.get(0);
        Object[] mm = em.createQuery("SELECT MIN(ds.clientsSum), MAX(ds.clientsSum), " +
                "COUNT(ds) FROM DeviceSurvey ds WHERE ds.frequency = :df AND " +
                "ds.timestamp >= :begin AND ds.timestamp < :eee", Object[].class)
                .setParameter("eee", end)
                .setParameter("df", df)
                .setParameter("begin", begin)
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
    public List<MinmaxSurveyData> getMultiMinmax(long deviceId, int frequencyMhz, int start, int groups, int groupTime) {
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
                list.add( getMinmax(deviceId, frequencyMhz, begin, begin + groupTime, em) );
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
    public Map<Long, CurrentDeviceState> xAll() {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Map<Long, CurrentDeviceState> m = xAll(em);
            tran.commit();
            return m;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public Map<Long, CurrentDeviceState> xSome(Collection<Long> devices) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Map<Long, CurrentDeviceState> m = xSome(devices, em);
            tran.commit();
            return m;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public CurrentDeviceState xOne(Long deviceId) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            CurrentDeviceState c = xOne(deviceId, em);
            tran.commit();
            return c;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    // mapuje id urządzenia do CurrentDeviceState
    public Map<Long, CurrentDeviceState> xAll(EntityManager em) {
        List<Long> bestSurveys = getBestDeviceSurveys(em);
        if(bestSurveys.isEmpty())bestSurveys.add(-1L);//!
        HashMap<Long, CurrentDeviceState> map = new HashMap<>();
        em.createQuery("SELECT sur, freq, dev " +
                        "FROM DeviceSurvey sur " +
                        "RIGHT JOIN sur.frequency freq " +
                        "RIGHT JOIN freq.device dev " +
                        "WHERE sur.id IN (:best)",
                Object[].class)
                .setParameter("best", bestSurveys)
                .getResultList()
                .forEach( arr -> {
                    DeviceSurvey sur = (DeviceSurvey)arr[0];
                    DeviceFrequency freq = (DeviceFrequency)arr[1];
                    Device dev = (Device)arr[2];
                    CurrentDeviceState current = new CurrentDeviceState(dev, freq, sur);
                    CurrentDeviceState found = map.get(dev.getId());
                    if(found != null) {
                        found.merge(current);
                    }
                    else {
                        map.put(dev.getId(), current);
                    }
                } );
        return map;
    }

    // mapuje id urządzenia do CurrentDeviceState
    public Map<Long, CurrentDeviceState> xSome(Collection<Long> devices, EntityManager em) {
        if(devices.isEmpty()) {
            return new HashMap<>();
        }
        List<Long> bestSurveys = getBestDeviceSurveys(em);
        if(bestSurveys.isEmpty())bestSurveys.add(-1L);//!
        HashMap<Long, CurrentDeviceState> map = new HashMap<>();
        //! left join i where in best
        //TODO czy to będzie szybko działać przy wielu badaniach?
        //TODO fetch join controller
        List<Object[]> list = em.createQuery("SELECT dev, freq, sur " +
                        "FROM Device dev " +
                        "LEFT JOIN dev.frequencyList freq " +
                        "LEFT JOIN freq.surveyList sur " +
                        "WHERE dev.id IN (:devices) AND ( sur.id IS NULL OR sur.id IN (:best) )",
                Object[].class)
                .setParameter("devices", devices)
                .setParameter("best", bestSurveys)
                .getResultList();
        log.info("list.size={}", list.size());
        list.forEach( arr -> {
            Device dev = (Device)arr[0];
            DeviceFrequency freq = (DeviceFrequency)arr[1];
            DeviceSurvey sur = (DeviceSurvey)arr[2];
            CurrentDeviceState current = new CurrentDeviceState(dev, freq, sur);
            CurrentDeviceState found = map.get(dev.getId());
            if(found != null) {
                found.merge(current);
            }
            else {
                map.put(dev.getId(), current);
            }
        } );
        return map;
    }

    // zwraca null jak nie znajdzie
    public CurrentDeviceState xOne(Long deviceId, EntityManager em) {
        List<Long> bestSurveys = getBestDeviceSurveys(em);
        if(bestSurveys.isEmpty())bestSurveys.add(-1L);//!
        CurrentDeviceState result = null;
        List<Object[]> list = em.createQuery("SELECT sur, freq, dev " +
                        "FROM DeviceSurvey sur " +
                        "RIGHT JOIN sur.frequency freq " +
                        "RIGHT JOIN freq.device dev " +
                        "WHERE sur.id IN (:best) AND dev.id = :deviceId",
                Object[].class)
                .setParameter("best", bestSurveys)
                .setParameter("deviceId", deviceId)
                .getResultList();
        for(Object[] arr : list) {
            DeviceSurvey sur = (DeviceSurvey) arr[0];
            DeviceFrequency freq = (DeviceFrequency) arr[1];
            Device dev = (Device) arr[2];
            CurrentDeviceState current = new CurrentDeviceState(dev, freq, sur);
            if (result != null) {
                result.merge(current);
            } else {
                result = current;
            }
        }
        return result;
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

        final List<SurveyInfo> originalSurveys = networkService.queryDevices(ipv4);
        if(originalSurveys.isEmpty()) {
            log.info("survey of controller (id={}, ip={}) returned no devices", controllerId, ipv4);
            return 0;
        }
        final TreeSet<SurveyInfo> uniqueSurveys = new TreeSet<>(new NameFrequencyUnique());
        for(SurveyInfo info : originalSurveys) {
            uniqueSurveys.add(info);
        }
        log.info("originalSurveys.size = {}", originalSurveys.size());
        log.info("uniqueSurveys.size = {}", uniqueSurveys.size());

        em = null;
        tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller controller = em.find(Controller.class, controllerId);
            if (controller == null)
                throw new NotFoundException("controller");

            final HashMap<String, Device> name2device = makeDevices(uniqueSurveys, controller, em);
            final Map<Long, CurrentDeviceState> id2current = xSome(
                    name2device
                            .entrySet()
                            .stream()
                            .map( e -> e.getValue().getId() )
                            .collect(Collectors.toSet()),
                    em
            );
            log.info("id2current.size() = {}", id2current.size());
            final List<DeviceSurvey> ds2persist = new ArrayList<>();
            for(final SurveyInfo info : uniqueSurveys) {
                final Device device = name2device.get(info.getName());
                final CurrentDeviceState current = id2current.get(device.getId());
                final DeviceFrequency frequency = current.findFrequency(info.getFrequencyMhz());
                final DeviceSurvey previousSurvey = current.findSurvey(info.getFrequencyMhz());
                final DeviceSurvey sur = new DeviceSurvey();
                sur.setTimestamp(timestamp);
                sur.setEnabled(info.isEnabled());
                sur.setClientsSum(info.getClientsSum());
                sur.setFrequency(frequency);
                if(previousSurvey == null) {
                    sur.setCumulative(0L);
                    ds2persist.add(sur);
                }
                else {
                    int prevTime = previousSurvey.getTimestamp();
                    int prevClients = previousSurvey.getClientsSum();
                    long prevCumulative = previousSurvey.getCumulative();
                    if( prevTime != timestamp ) {
                        sur.setCumulative( prevCumulative + prevClients * ( timestamp - prevTime ) );
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

    private List<Long> getBestDeviceSurveys(EntityManager em) {
        List list = em.createNativeQuery("SELECT ds.id FROM device_survey ds INNER JOIN ( " +
                "SELECT frequency_id, MAX(`timestamp`) AS m FROM device_survey GROUP BY frequency_id " +
                ") best ON ds.frequency_id = best.frequency_id AND ds.`timestamp` = best.m")
        .getResultList();
        List<Long> result = new ArrayList<>();
        for(Object obj : list) {
            Number id = (Number)obj;
            result.add( id.longValue() );
        }
        return result;
    }

    /**
     * Dla każdej nazwy z <code>list</code> wstawia do bazy nowe urządzenie jeśli takie nie istnieje.
     * @return mapa [nazwa urządzenia => urządzenie z bazy]
     */
    private HashMap<String, Device> makeDevices(Collection<SurveyInfo> surveys, Controller controller, EntityManager em) {
        if(surveys.isEmpty())return new HashMap<>();
        final HashMap<String, Device> existing = new HashMap<>();
        for(SurveyInfo si : surveys) {
            existing.put(si.getName(), null);
        }
        em.createQuery("SELECT d FROM Device d LEFT JOIN FETCH d.frequencyList WHERE d.name IN (:names)", Device.class)
                .setParameter("names", existing.keySet())
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
        em.flush();
        for(SurveyInfo si : surveys) {
            final Device d = result.get( si.getName() );
            final List<DeviceFrequency> freqList = d.getFrequencyList();
            DeviceFrequency freq = null;
            for(DeviceFrequency test : freqList) {
                if( (int)test.getFrequency() == si.getFrequencyMhz() ) {
                    freq = test;
                    break;
                }
            }
            if(freq == null) {
                freq = new DeviceFrequency();
                freq.setDevice(d);
                freq.setFrequency(si.getFrequencyMhz());
                freqList.add(freq);
                em.persist(freq);
            }
        }
        em.flush();
        return result;
    }

    public static class NameFrequencyUnique implements Comparator<SurveyInfo> {
        @Override
        public int compare(SurveyInfo a, SurveyInfo b) {
            if(a.getFrequencyMhz() == b.getFrequencyMhz()) {
                return a.getName().compareTo(b.getName());
            }
            return a.getFrequencyMhz() - b.getFrequencyMhz();
        }
    }
}