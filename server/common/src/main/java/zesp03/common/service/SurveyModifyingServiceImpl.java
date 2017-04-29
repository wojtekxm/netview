package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.ShortSurvey;
import zesp03.common.data.SurveyInfo;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;
import zesp03.common.exception.NotFoundException;
import zesp03.common.repository.ControllerRepository;
import zesp03.common.repository.DeviceFrequencyRepository;
import zesp03.common.repository.DeviceRepository;
import zesp03.common.repository.DeviceSurveyRepository;
import zesp03.common.util.SurveyInfoCollection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.*;

@Service
@Transactional
public class SurveyModifyingServiceImpl implements SurveyModifyingService {
    private static final Logger log = LoggerFactory.getLogger(SurveyModifyingServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private NetworkService networkService;

    @Autowired
    private SurveyReadingService surveyReadingService;

    @Autowired
    private ControllerRepository controllerRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceFrequencyRepository deviceFrequencyRepository;

    @Autowired
    private DeviceSurveyRepository deviceSurveyRepository;

    @Override
    public int examineAll() {
        Iterable<Controller> list = controllerRepository.findAllNotDeleted();
        int result = 0;
        for (Controller c : list) {
            try {
                result += examineOne(c);
            } catch (RuntimeException exc) {
                log.error("failed to examine controller", exc);
            }
        }
        return result;
    }

    @Override
    public int examineOne(long controllerId) {
        Optional<Controller> opt = controllerRepository.findOneNotDeleted(controllerId);
        if(!opt.isPresent()) {
            throw new NotFoundException("controller");
        }
        return examineOne(opt.get());
    }

    private int examineOne(Controller controller) {
        final List<SurveyInfo> originalSurveys =
                networkService.queryDevices(controller.getIpv4(), controller.getCommunity());
        final SurveyInfoCollection col = new SurveyInfoCollection(originalSurveys);
        return saveToDatabase(controller, col);
    }

    private int saveToDatabase(Controller controller, SurveyInfoCollection collection) {
        final int timestamp = (int) Instant.now().getEpochSecond();
        makeDevices(collection, controller);
        final Map<Long, CurrentDeviceState> id2current = surveyReadingService.checkForController(controller);
        final List<DeviceSurvey> ds2persist = new ArrayList<>();
        id2current.forEach( (deviceId, currentState) -> {
            for(Integer frequencyMhz : currentState.getFrequencies()) {
                final DeviceFrequency deviceFrequency = currentState.findFrequency(frequencyMhz);
                final DeviceSurvey previousSurvey = currentState.findSurvey(frequencyMhz);
                final SurveyInfo info = collection.find(
                        currentState.getDevice().getName(), frequencyMhz);
                final DeviceSurvey newSurvey = new DeviceSurvey();
                newSurvey.setTimestamp(timestamp);
                if(info != null) {
                    newSurvey.setEnabled(true);
                    newSurvey.setClientsSum(info.getClientsSum());
                }
                else {
                    newSurvey.setEnabled(false);
                    newSurvey.setClientsSum(0);
                }
                newSurvey.setFrequency(deviceFrequency);
                newSurvey.setDeleted(false);
                if(previousSurvey == null) {
                    ds2persist.add(newSurvey);
                }
                else {
                    final int prevTime = previousSurvey.getTimestamp();
                    if(prevTime > timestamp) {
                        log.warn("last survey is from the future, last timestamp = {}, " +
                                        "current timestamp = {}, DeviceFrequency.id = {}",
                                prevTime, timestamp, deviceFrequency.getId());
                    }
                    if(prevTime < timestamp) {
                        ds2persist.add(newSurvey);
                    }
                }
            }
        } );
        for(DeviceSurvey ds : ds2persist) {
            em.persist(ds);
        }
        return ds2persist.size();
    }

    /*private int saveToDatabase(Controller controller, HashSet<SurveyInfo> uniqueSurveys) {
        final int timestamp = (int) Instant.now().getEpochSecond();
        final Map<String, Device> name2device = makeDevices(uniqueSurveys, controller);
        final Map<Long, CurrentDeviceState> id2current = surveyReadingService.checkForController(controller);
        final List<DeviceSurvey> ds2persist = new ArrayList<>();
        for(final SurveyInfo info : uniqueSurveys) {
            final Device device = name2device.get(info.getName());
            final CurrentDeviceState current = id2current.get(
                    device.getId()
            );
            final DeviceFrequency frequency = current.findFrequency(
                    info.getFrequencyMhz()
            );
            final DeviceSurvey previousSurvey = current.findSurvey(
                    info.getFrequencyMhz()
            );
            final DeviceSurvey sur = new DeviceSurvey();
            sur.setTimestamp(timestamp);
            sur.setEnabled(info.isEnabled());
            sur.setClientsSum(info.getClientsSum());
            sur.setFrequency(frequency);
            sur.setDeleted(false);
            if(previousSurvey == null) {
                ds2persist.add(sur);
            }
            else {
                final int prevTime = previousSurvey.getTimestamp();
                if(prevTime > timestamp) {
                    log.warn("last survey is from the future, last timestamp = {}, " +
                                    "current timestamp = {}, DeviceFrequency.id = {}",
                            prevTime, timestamp, frequency.getId());
                }
                if(prevTime < timestamp) {
                    ds2persist.add(sur);
                }
            }
        }
        for(DeviceSurvey ds : ds2persist) {
            em.persist(ds);
        }
        return ds2persist.size();
    }*/

    @Override
    public Map<String, Device> makeDevices(Iterable<SurveyInfo> surveys, Controller controller) {
        final HashMap<String, Device> existing = new HashMap<>();
        for(SurveyInfo si : surveys) {
            existing.put(si.getName(), null);
        }
        if(existing.isEmpty()) {
            return new HashMap<>(0);
        }
        em.createQuery("SELECT d FROM Device d LEFT JOIN FETCH d.frequencyList WHERE " +
                "d.name IN (:names) AND d.deleted = FALSE", Device.class)
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
                d.setDeleted(false);
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
                freq.setDeleted(false);
                freqList.add(freq);
                em.persist(freq);
            }
        }
        em.flush();
        return result;
    }

    @Override
    public void importSurveys(Long deviceId, Integer frequencyMhz, List<ShortSurvey> data) {
        data.sort(Comparator.comparingInt(ShortSurvey::getTimestamp));
        Optional<DeviceFrequency> opt = deviceFrequencyRepository.findByDeviceAndFrequencyNotDeleted(deviceId, frequencyMhz);
        DeviceFrequency df;
        if(opt.isPresent()) {
            df = opt.get();
        }
        else {
            Optional<Device> optDevice = deviceRepository.findOneNotDeleted(deviceId);
            if(!opt.isPresent()) {
                throw new NotFoundException("device");
            }
            df = new DeviceFrequency();
            df.setDevice(optDevice.get());
            df.setFrequency(frequencyMhz);
            df.setDeleted(false);
            deviceFrequencyRepository.save(df);
        }
        deviceSurveyRepository.markDeletedAll(df);
        DeviceSurvey lastDS = null;
        for(ShortSurvey shortSurvey : data) {
            if(shortSurvey.getTimestamp() < 0) {
                throw new IllegalArgumentException("survey time < 0");
            }
            if( (lastDS != null) &&
                    (shortSurvey.getTimestamp() <= lastDS.getTimestamp()) )continue;
            DeviceSurvey deviceSurvey = new DeviceSurvey();
            deviceSurvey.setFrequency(df);
            deviceSurvey.setClientsSum(shortSurvey.getClients());
            deviceSurvey.setTimestamp(shortSurvey.getTimestamp());
            deviceSurvey.setEnabled(shortSurvey.isEnabled());
            deviceSurvey.setDeleted(false);
            deviceSurveyRepository.save(deviceSurvey);
            lastDS = deviceSurvey;
        }
    }
}
