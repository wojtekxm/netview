package zesp03.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.entity.DeviceSurvey;
import zesp03.common.repository.DeviceSurveyRepository;
import zesp03.common.service.DeviceFrequencyService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
@Deprecated
public class TestService {
    private static final Logger log = LoggerFactory.getLogger(TestService.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DeviceFrequencyService deviceFrequencyService;

    @Autowired
    private DeviceSurveyRepository deviceSurveyRepository;

    public void markTest(Long deviceId, int frequencyMhz, int validAfter) {
        Long fi = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        em.createQuery("UPDATE DeviceSurvey ds SET ds.clientsSum=-1 WHERE " +
                "ds.frequency.id = :fi AND ds.timestamp <= :after")
                .setParameter("after", validAfter)
                .setParameter("fi", fi)
                .executeUpdate();
    }

    public void justDelete(Long deviceId, int frequencyMhz, int validAfter) {
        Long fi = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        em.createQuery("DELETE FROM DeviceSurvey ds WHERE " +
                "ds.frequency.id = :fi AND ds.timestamp <= :after")
                .setParameter("after", validAfter)
                .setParameter("fi", fi)
                .executeUpdate();
    }

    public void selectAndDelete(Long deviceId, int frequencyMhz, int validAfter) {
        Long fi = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
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
        Long fi = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        List<DeviceSurvey> list = deviceSurveyRepository.findFromPeriodOrderByTime(fi, 0, 2000000000);
        for(DeviceSurvey ds : list) {
            ds.getTimestamp();
        }
        log.info("list size = {}", list.size());
    }
}
