package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.SurveyInfoUniqueNameFrequency;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;
import zesp03.common.exception.NotFoundException;
import zesp03.common.repository.ControllerRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SurveySavingServiceImpl implements SurveySavingService {
    private static final Logger log = LoggerFactory.getLogger(SurveySavingServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private NetworkService networkService;

    @Autowired
    private CurrentSurveyService currentSurveyService;

    @Autowired
    private ControllerRepository controllerRepository;

    @Override
    public int examineAll() {
        Iterable<Controller> list = controllerRepository.findAll();
        int result = 0;
        for (Controller c : list) {
            try {
                result += examineOne(c.getId());
            } catch (RuntimeException exc) {
                log.error("failed to examine controller", exc);
            }
        }
        return result;
    }

    @Override
    public int examineOne(long controllerId) {
        Controller controller = controllerRepository.findOne(controllerId);
        if(controller == null) {
            throw new NotFoundException("controller");
        }
        final List<SurveyInfoUniqueNameFrequency> originalSurveys = networkService.queryDevices(controller.getIpv4());
        if (originalSurveys.isEmpty()) {
            log.info("survey of controller (id={}, ip={}) returned no devices", controllerId, controller.getIpv4());
            return 0;
        }
        final HashSet<SurveyInfoUniqueNameFrequency> uniqueSurveys = new HashSet<>();
        for (SurveyInfoUniqueNameFrequency info : originalSurveys) {
            uniqueSurveys.add(info);
        }
        return saveToDatabase(controllerId, uniqueSurveys);
    }

    public int saveToDatabase(Long controllerId, HashSet<SurveyInfoUniqueNameFrequency> uniqueSurveys) {
        final int timestamp = (int) Instant.now().getEpochSecond();
        final Controller controller = controllerRepository.findOne(controllerId);
        if(controller == null) {
            throw new NotFoundException("controller");
        }
        final HashMap<String, Device> name2device = makeDevices(uniqueSurveys, controller);
        final Map<Long, CurrentDeviceState> id2current = currentSurveyService.checkSome(name2device.entrySet()
                .stream()
                .map( e -> e.getValue().getId() )
                .collect(Collectors.toSet())
        );
        final List<DeviceSurvey> ds2persist = new ArrayList<>();
        for(final SurveyInfoUniqueNameFrequency info : uniqueSurveys) {
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
        return ds2persist.size();
    }

    /**
     * Dla każdej nazwy z <code>list</code> wstawia do bazy nowe urządzenie jeśli takie nie istnieje.
     * @return mapa [nazwa urządzenia => urządzenie z bazy]
     */
    public HashMap<String, Device> makeDevices(Collection<SurveyInfoUniqueNameFrequency> surveys, Controller controller) {
        if(surveys.isEmpty())return new HashMap<>();
        final HashMap<String, Device> existing = new HashMap<>();
        for(SurveyInfoUniqueNameFrequency si : surveys) {
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
        for(SurveyInfoUniqueNameFrequency si : surveys) {
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
