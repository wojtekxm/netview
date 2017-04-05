package zesp03.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;
import zesp03.common.repository.DeviceSurveyRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CurrentSurveyServiceImpl implements CurrentSurveyService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DeviceSurveyRepository deviceSurveyRepository;

    @Override
    public List<Long> findLastIds() {
        return deviceSurveyRepository.findLastIds()
                .stream()
                .map( number -> number.longValue() )
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CurrentDeviceState> checkOne(Long deviceId) {
        List<Long> bestSurveys = findLastIds();
        if(bestSurveys.isEmpty()) {
            bestSurveys.add(-1L);//!
        }
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
        List<Long> bestSurveys = findLastIds();
        if(bestSurveys.isEmpty()) {
            bestSurveys.add(-1L);//!
        }
        HashMap<Long, CurrentDeviceState> map = new HashMap<>();
        //TODO ! left join i where in best
        //TODO czy to będzie szybko działać przy wielu badaniach?
        //TODO fetch join controller
        List<Object[]> list = em.createQuery("SELECT dev, freq, sur " +
                        "FROM Device dev " +
                        "LEFT JOIN dev.frequencyList freq " +
                        "LEFT JOIN freq.surveyList sur " +
                        "WHERE dev.id IN (:deviceIds) AND ( sur.id IS NULL OR sur.id IN (:best) )",
                Object[].class)
                .setParameter("deviceIds", deviceIds)
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

    @Override
    public Map<Long, CurrentDeviceState> checkAll() {
        List<Long> bestSurveys = findLastIds();
        if(bestSurveys.isEmpty()) {
            bestSurveys.add(-1L);//!
        }
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
}
