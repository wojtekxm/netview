package zesp03.common.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
@Transactional
public class CurrentSurveyServiceImpl implements CurrentSurveyService {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<CurrentDeviceState> checkOne(Long deviceId) {
        CurrentDeviceState result = null;
        List<Object[]> list = em.createQuery("SELECT dev, df, sur FROM Device dev " +
                        "LEFT JOIN DeviceFrequency df ON dev.id = df.device.id " +
                        "LEFT JOIN ViewLastSurvey vfs ON df.id = vfs.frequencyId " +
                        "LEFT JOIN DeviceSurvey sur ON vfs.surveyId = sur.id " +
                        "WHERE dev.id = :deviceId",
                Object[].class)
                .setParameter("deviceId", deviceId)
                .getResultList();
        for(Object[] arr : list) {
            Device dev = (Device)arr[0];
            DeviceFrequency df = (DeviceFrequency)arr[1];
            DeviceSurvey sur = (DeviceSurvey)arr[2];
            CurrentDeviceState current = new CurrentDeviceState(dev, df, sur);
            if (result != null) {
                result.merge(current);
            } else {
                result = current;
            }
        }
        if(result != null) {
            return Optional.of(result);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Map<Long, CurrentDeviceState> checkSome(Collection<Long> deviceIds) {
        if(deviceIds.isEmpty()) {
            return new HashMap<>();
        }
        HashMap<Long, CurrentDeviceState> map = new HashMap<>();
        em.createQuery("SELECT dev, df, sur FROM Device dev " +
                        "LEFT JOIN DeviceFrequency df ON dev.id = df.device.id " +
                        "LEFT JOIN ViewLastSurvey vfs ON df.id = vfs.frequencyId " +
                        "LEFT JOIN DeviceSurvey sur ON vfs.surveyId = sur.id " +
                        "WHERE dev.id IN (:deviceIds)",
                Object[].class)
                .setParameter("deviceIds", deviceIds)
                .getResultList()
                .forEach( arr -> {
                    Device dev = (Device)arr[0];
                    DeviceFrequency df = (DeviceFrequency)arr[1];
                    DeviceSurvey sur = (DeviceSurvey)arr[2];
                    CurrentDeviceState current = new CurrentDeviceState(dev, df, sur);
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

    @Override
    public Map<Long, CurrentDeviceState> checkAll() {
        HashMap<Long, CurrentDeviceState> map = new HashMap<>();
        em.createQuery("SELECT dev, df, sur FROM Device dev " +
                "LEFT JOIN DeviceFrequency df ON dev.id = df.device.id " +
                "LEFT JOIN ViewLastSurvey vfs ON df.id = vfs.frequencyId " +
                "LEFT JOIN DeviceSurvey sur ON vfs.surveyId = sur.id",
                Object[].class)
                .getResultList()
                .forEach( arr -> {
                    Device dev = (Device)arr[0];
                    DeviceFrequency df = (DeviceFrequency)arr[1];
                    DeviceSurvey sur = (DeviceSurvey)arr[2];
                    CurrentDeviceState current = new CurrentDeviceState(dev, df, sur);
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
}
