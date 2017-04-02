package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.core.Database;
import zesp03.common.data.*;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;
import zesp03.common.exception.NotFoundException;
import zesp03.common.repository.DeviceFrequencyRepository;
import zesp03.common.repository.DeviceRepository;
import zesp03.common.repository.DeviceSurveyRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService {
    private static final Logger log = LoggerFactory.getLogger(SurveyServiceImpl.class);

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceFrequencyRepository deviceFrequencyRepository;

    @Autowired
    private DeviceSurveyRepository deviceSurveyRepository;

    @Autowired
    private DeviceFrequencyService deviceFrequencyService;

    @Autowired
    private NetworkService networkService;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<ShortSurvey> getOriginal(long deviceId, int frequencyMhz, int start, int end) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");

        Long frequencyId = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        return deviceSurveyRepository.findFromPeriod(frequencyId, start, end)
                .stream()
                .map(ShortSurvey::make)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SurveyPeriodAvg getAverage(long deviceId, int frequencyMhz, int start, int end) {
        Long frequencyId = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        return getAverage(frequencyId, start, end);
    }

    @Override
    @Transactional
    public SurveyPeriodAvgMinMax getAvgMinMax(long deviceId, int frequencyMhz, int start, int end) {
        Long frequencyId = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        return getAvgMinMax(frequencyId, start, end);
    }

    @Override
    @Transactional
    public SurveyPeriodMinMax getMinMax(long deviceId, int frequencyMhz, int start, int end) {
        Long frequencyId = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        return getMinMax(frequencyId, start, end);
    }

    @Transactional
    public SurveyPeriodAvg getAverage(Long frequencyId, int start, int end) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");

        final List<DeviceSurvey> listEnd = deviceSurveyRepository.findLastNotAfter(frequencyId, end);
        if( listEnd.isEmpty() ) {
            return null;
        }
        final DeviceSurvey dsEnd = listEnd.get(0);
        final long timeEnd = end;
        final long cumulativeEnd = dsEnd.getCumulative() + (long)dsEnd.getClientsSum() * (timeEnd - dsEnd.getTimestamp());
        final List<DeviceSurvey> listStart = deviceSurveyRepository.findLastNotAfter(frequencyId, start);
        long timeStart, cumulativeStart;
        if( listStart.isEmpty() ) {
            final DeviceSurvey first = deviceSurveyRepository.findFirst(frequencyId).get(0);
            timeStart = first.getTimestamp();
            cumulativeStart = first.getCumulative();
        }
        else {
            final DeviceSurvey dsStart = listStart.get(0);
            timeStart = start;
            cumulativeStart = dsStart.getCumulative() + (long)dsStart.getClientsSum() * (timeStart - dsStart.getTimestamp());
        }
        double avg = (double) (cumulativeEnd - cumulativeStart) / (timeEnd - timeStart);
        if(avg < 0.0) {
            log.warn("avg < 0; frequencyId={} start={} end={} cumulativeStart={} cumulativeEnd={} timeStart={} timeEnd={} avg={}",
                    frequencyId, start, end, cumulativeStart, cumulativeEnd, timeStart, timeEnd, avg);
            avg = 0.0;
        }
        SurveyPeriodAvg result = new SurveyPeriodAvg();
        result.setTimeStart((int)timeStart);
        result.setTimeEnd((int)timeEnd);
        result.setAverage(avg);
        return result;
    }

    @Transactional
    public SurveyPeriodMinMax getMinMax(Long frequencyId, int start, int end) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");

        Integer begin;
        int timeStart;
        final List<DeviceSurvey> list = deviceSurveyRepository.findLastNotAfter(frequencyId, start);
        if( list.isEmpty() ) {
            List<DeviceSurvey> listFirst = deviceSurveyRepository.findFirst(frequencyId);
            if( listFirst.isEmpty() ) {
                return null;
            }
            begin = listFirst.get(0).getTimestamp();
            timeStart = begin;
        }
        else {
            begin = list.get(0).getTimestamp();
            timeStart = start;
        }

        //TODO COALESCE ?
        Object[] row = em.createQuery("SELECT MIN(ds.clientsSum), MAX(ds.clientsSum), " +
                "COUNT(ds) FROM DeviceSurvey ds WHERE ds.frequency.id = :frequencyId AND " +
                "ds.timestamp >= :t0 AND ds.timestamp < :t1", Object[].class)
                .setParameter("frequencyId", frequencyId)
                .setParameter("t0", begin)
                .setParameter("t1", end)
                .getSingleResult();
        Number min = (Number)row[0];
        Number max = (Number)row[1];
        Number span = (Number)row[2];
        if(min == null || max == null || span == null) {
            return null;
        }
        SurveyPeriodMinMax result = new SurveyPeriodMinMax();
        result.setMin(min.intValue());
        result.setMax(max.intValue());
        result.setSurveySpan(span.intValue());
        result.setTimeStart(timeStart);
        result.setTimeEnd(end);
        return result;
    }

    @Transactional
    public SurveyPeriodAvgMinMax getAvgMinMax(Long frequencyId, int start, int end) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");

        final List<DeviceSurvey> listEnd = deviceSurveyRepository.findLastNotAfter(frequencyId, end);
        if( listEnd.isEmpty() ) {
            return null;
        }
        final DeviceSurvey dsEnd = listEnd.get(0);
        final long timeEnd = end;
        final long cumulativeEnd = dsEnd.getCumulative() + (long)dsEnd.getClientsSum() * (timeEnd - dsEnd.getTimestamp());

        List<DeviceSurvey> listStart = deviceSurveyRepository.findLastNotAfter(frequencyId, start);
        if( listStart.isEmpty() ) {
            listStart = deviceSurveyRepository.findFirst(frequencyId);
        }
        final DeviceSurvey dsStart = listStart.get(0);
        final Integer beginMinMax = dsStart.getTimestamp();
        long timeStart, cumulativeStart;
        if(dsStart.getTimestamp() < start) {
            timeStart = start;
            cumulativeStart = dsStart.getCumulative() + (long)dsStart.getClientsSum() * (timeStart - dsStart.getTimestamp());
        }
        else {
            timeStart = dsStart.getTimestamp();
            cumulativeStart = dsStart.getCumulative();
        }
        double avg = (double) (cumulativeEnd - cumulativeStart) / (timeEnd - timeStart);
        if(avg < 0.0) {
            log.warn("avg < 0; frequencyId={} start={} end={} cumulativeStart={} cumulativeEnd={} timeStart={} timeEnd={} avg={}",
                    frequencyId, start, end, cumulativeStart, cumulativeEnd, timeStart, timeEnd, avg);
            avg = 0.0;
        }

        //TODO COALESCE ?
        Object[] row = em.createQuery("SELECT MIN(ds.clientsSum), MAX(ds.clientsSum), " +
                "COUNT(ds) FROM DeviceSurvey ds WHERE ds.frequency.id = :frequencyId AND " +
                "ds.timestamp >= :t0 AND ds.timestamp < :t1", Object[].class)
                .setParameter("frequencyId", frequencyId)
                .setParameter("t0", beginMinMax)
                .setParameter("t1", (int)timeEnd)
                .getSingleResult();
        Number min = (Number)row[0];
        Number max = (Number)row[1];
        Number span = (Number)row[2];
        if(min == null || max == null || span == null) {
            return null;
        }
        SurveyPeriodAvgMinMax result = new SurveyPeriodAvgMinMax();
        result.setMin(min.intValue());
        result.setMax(max.intValue());
        result.setSurveySpan(span.intValue());
        result.setTimeStart((int)timeStart);
        result.setTimeEnd((int)timeEnd);
        result.setAverage(avg);
        return result;
    }

    @Override
    @Transactional
    public List<SurveyPeriodAvg> getMultiAverage(long deviceId, int frequencyMhz, int start, int end, int groupTime) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");
        if(groupTime < 1)
            throw new IllegalArgumentException("groupTime < 1");

        final Long frequencyId = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        int begin = start;
        final List<SurveyPeriodAvg> list = new ArrayList<>();
        while(begin < end) {
            if(begin < 0) {
                throw new IllegalArgumentException("start time overflow");
            }
            final SurveyPeriodAvg a = getAverage(frequencyId, begin, begin + groupTime);
            if(a != null) {
                list.add(a);
            }
            begin += groupTime;
        }
        return list;
    }

    @Override
    @Transactional
    public List<SurveyPeriodMinMax> getMultiMinMax(long deviceId, int frequencyMhz, int start, int end, int groupTime) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");
        if(groupTime < 1)
            throw new IllegalArgumentException("groupTime < 1");

        final Long frequencyId = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        int begin = start;
        final List<SurveyPeriodMinMax> list = new ArrayList<>();
        while(begin < end) {
            final SurveyPeriodMinMax m = getMinMax(frequencyId, begin, begin + groupTime);
            if(m != null) {
                list.add(m);
            }
            begin += groupTime;
        }
        return list;
    }

    @Override
    @Transactional
    public List<SurveyPeriodAvgMinMax> getMultiAvgMinMax(long deviceId, int frequencyMhz, int start, int end, int groupTime) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");
        if(groupTime < 1)
            throw new IllegalArgumentException("groupTime < 1");

        final Long frequencyId = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        int begin = start;
        final List<SurveyPeriodAvgMinMax> list = new ArrayList<>();
        while(begin < end) {
            final SurveyPeriodAvgMinMax x = getAvgMinMax(frequencyId, begin, begin + groupTime);
            if(x != null) {
                list.add(x);
            }
            begin += groupTime;
        }
        return list;
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
        //TODO ! left join i where in best
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
        final TreeSet<SurveyInfo> uniqueSurveys = new TreeSet<>(new SurveyInfo.NameFrequencyUnique());
        for(SurveyInfo info : originalSurveys) {
            uniqueSurveys.add(info);
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

            final HashMap<String, Device> name2device = makeDevices(uniqueSurveys, controller, em);
            final Map<Long, CurrentDeviceState> id2current = xSome(
                    name2device
                            .entrySet()
                            .stream()
                            .map( e -> e.getValue().getId() )
                            .collect(Collectors.toSet()),
                    em
            );
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
}