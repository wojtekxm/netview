package zesp03.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.SampleRaw;
import zesp03.common.data.SurveyInfo;
import zesp03.common.entity.Controller;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;
import zesp03.common.repository.ControllerRepository;
import zesp03.common.repository.DeviceSurveyRepository;
import zesp03.common.service.SurveyModifyingService;
import zesp03.common.service.SurveyReadingService;
import zesp03.webapp.dto.DeviceDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Deprecated
public class TestService {
    private static final Logger log = LoggerFactory.getLogger(TestService.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SurveyReadingService surveyReadingService;

    @Autowired
    private SurveyModifyingService surveyModifyingService;

    @Autowired
    private ControllerRepository controllerRepository;

    @Autowired
    private DeviceSurveyRepository deviceSurveyRepository;

    public void markTest(Long deviceId, int frequencyMhz, int validAfter) {
        Long fi = surveyReadingService.getFrequencyIdNotDeletedOrThrow(deviceId, frequencyMhz);
        em.createQuery("UPDATE DeviceSurvey ds SET ds.clientsSum=-1 WHERE " +
                "ds.frequency.id = :fi AND ds.timestamp <= :after")
                .setParameter("after", validAfter)
                .setParameter("fi", fi)
                .executeUpdate();
    }

    public void justDelete(Long deviceId, int frequencyMhz, int validAfter) {
        Long fi = surveyReadingService.getFrequencyIdNotDeletedOrThrow(deviceId, frequencyMhz);
        em.createQuery("DELETE FROM DeviceSurvey ds WHERE " +
                "ds.frequency.id = :fi AND ds.timestamp <= :after")
                .setParameter("after", validAfter)
                .setParameter("fi", fi)
                .executeUpdate();
    }

    public void selectAndDelete(Long deviceId, int frequencyMhz, int validAfter) {
        Long fi = surveyReadingService.getFrequencyIdNotDeletedOrThrow(deviceId, frequencyMhz);
        List<DeviceSurvey> l = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE " +
                "ds.frequency.id = :fi AND ds.timestamp <= :after", DeviceSurvey.class)
                .setParameter("after", validAfter)
                .setParameter("fi", fi)
                .getResultList();
        for(DeviceSurvey ds : l) {
            deviceSurveyRepository.delete(ds);
        }
    }

    public void selectSurveys(Long deviceId, Integer frequencyMhz) {
        Long fi = surveyReadingService.getFrequencyIdNotDeletedOrThrow(deviceId, frequencyMhz);
        List<DeviceSurvey> list = deviceSurveyRepository.findFromPeriodNotDeletedOrderByTime(fi, 0, 2000000000);
        for(DeviceSurvey ds : list) {
            ds.getTimestamp();
        }
        log.info("list size = {}", list.size());
    }

    public void makeDevices(Long controllerId, String deviceName, int frequencyMhz) {
        Controller c = controllerRepository.findOne(controllerId);
        List<SurveyInfo> list = new ArrayList<>();
        final SurveyInfo si = new SurveyInfo(
                deviceName, frequencyMhz, -99);
        list.add(si);
        surveyModifyingService.makeDevices(c, list);
    }

    public Map<Long, TestCurrentDeviceState> checkAll() {
        Map<Long, TestCurrentDeviceState> result = new HashMap<>();
        surveyReadingService.checkAllFetch().forEach( (id, cds) -> {
            result.put(id, TestCurrentDeviceState.make(cds));
        } );
        return result;
    }

    public static class TestCurrentDeviceState {
        private DeviceDto device;
        private Map<Integer, SampleRaw> frequency = new HashMap<>();

        public DeviceDto getDevice() {
            return device;
        }

        public void setDevice(DeviceDto device) {
            this.device = device;
        }

        public Map<Integer, SampleRaw> getFrequency() {
            return frequency;
        }

        public void setFrequency(Map<Integer, SampleRaw> frequency) {
            this.frequency = frequency;
        }

        public void wrap(CurrentDeviceState cds) {
            this.device = DeviceDto.make(cds.getDevice());
            for(Integer mhz : cds.getFrequencies()) {
                DeviceFrequency df = cds.findFrequency(mhz);
                DeviceSurvey ds = cds.findSurvey(mhz);
                SampleRaw ss = new SampleRaw();
                ss.setTimestamp(ds.getTimestamp());
                ss.setClients(ds.getClientsSum());
                ss.setEnabled(ds.isEnabled());
                frequency.put(mhz, ss);
            }
        }

        public static TestCurrentDeviceState make(CurrentDeviceState cds) {
            TestCurrentDeviceState t = new TestCurrentDeviceState();
            t.wrap(cds);
            return t;
        }
    }
}
