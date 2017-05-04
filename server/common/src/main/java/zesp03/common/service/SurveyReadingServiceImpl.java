package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.ShortSurvey;
import zesp03.common.data.SurveyPeriodAvgMinMax;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;
import zesp03.common.exception.NotFoundException;
import zesp03.common.exception.ValidationException;
import zesp03.common.repository.DeviceFrequencyRepository;
import zesp03.common.repository.DeviceSurveyRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SurveyReadingServiceImpl implements SurveyReadingService {
    private static final Logger log = LoggerFactory.getLogger(SurveyReadingServiceImpl.class);

    @Autowired
    private DeviceSurveyRepository deviceSurveyRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    DeviceFrequencyRepository deviceFrequencyRepository;

    @Override
    public Long getFrequencyIdNotDeletedOrThrow(Long deviceId, Integer frequencyMhz) {
        Optional<DeviceFrequency> opt = deviceFrequencyRepository.findByDeviceAndFrequencyNotDeleted(deviceId, frequencyMhz);
        if( ! opt.isPresent() ) {
            throw new NotFoundException("device with this id and frequency");
        }
        return opt.get().getId();
    }

    @Override
    public Optional<CurrentDeviceState> checkOne(Long deviceId) {
        CurrentDeviceState result = null;
        List<Object[]> list = em.createQuery("SELECT dev, df, sur FROM Device dev " +
                        "LEFT JOIN DeviceFrequency df ON dev.id = df.device.id " +
                        "LEFT JOIN ViewLastSurvey vfs ON df.id = vfs.frequencyId " +
                        "LEFT JOIN DeviceSurvey sur ON vfs.surveyId = sur.id " +
                        "WHERE dev.id = :deviceId AND " +
                        "(dev.deleted = 0 OR dev.deleted IS NULL) AND " +
                        "( df.deleted = 0 OR  df.deleted IS NULL) AND " +
                        "(sur.deleted = 0 OR sur.deleted IS NULL)",
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
    public Map<Long, CurrentDeviceState> checkForController(Controller c) {
        HashMap<Long, CurrentDeviceState> map = new HashMap<>();
        List<Object[]> list = em.createQuery("SELECT dev, df, sur FROM Device dev " +
                        "LEFT JOIN DeviceFrequency df ON dev.id = df.device.id " +
                        "LEFT JOIN ViewLastSurvey vfs ON df.id = vfs.frequencyId " +
                        "LEFT JOIN DeviceSurvey sur ON vfs.surveyId = sur.id " +
                        "WHERE dev.controller = :c AND " +
                        "(dev.deleted = 0 OR dev.deleted IS NULL) AND " +
                        "( df.deleted = 0 OR  df.deleted IS NULL) AND " +
                        "(sur.deleted = 0 OR sur.deleted IS NULL)",
                Object[].class)
                .setParameter("c", c)
                .getResultList();
        for(Object[] arr : list) {
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
        }
        return map;
    }

    @Override
    public Map<Long, CurrentDeviceState> checkAllFetch() {
        HashMap<Long, CurrentDeviceState> map = new HashMap<>();
        em.createQuery("SELECT dev, df, sur FROM Device dev LEFT JOIN FETCH dev.controller con LEFT JOIN FETCH dev.building bui " +
                        "LEFT JOIN DeviceFrequency df ON dev.id = df.device.id " +
                        "LEFT JOIN ViewLastSurvey vfs ON df.id = vfs.frequencyId " +
                        "LEFT JOIN DeviceSurvey sur ON vfs.surveyId = sur.id WHERE " +
                        "(dev.deleted = 0 OR dev.deleted IS NULL) AND " +
                        "( df.deleted = 0 OR  df.deleted IS NULL) AND " +
                        "(sur.deleted = 0 OR sur.deleted IS NULL)",
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

    @Override
    public List<ShortSurvey> getOriginal(long deviceId, int frequencyMhz, int start, int end) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");

        Long frequencyId = getFrequencyIdNotDeletedOrThrow(deviceId, frequencyMhz);
        return deviceSurveyRepository.findFromPeriodNotDeletedOrderByTime(frequencyId, start, end)
                .stream()
                .map(ShortSurvey::make)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurveyPeriodAvgMinMax> getMultiAvgMinMax(final long deviceId, final int frequencyMhz, final int start, int end, final int groupTime) {
        if(start < 0) {
            throw new ValidationException("start", "less than 0");
        }
        if(start >= end) {
            throw new ValidationException("start", "less greater or equal end");
        }
        if(groupTime < 1) {
            throw new ValidationException("groupTime", "less than 1");
        }
        final int now = (int)Instant.now().getEpochSecond();
        if(end > now) {
            end = now + 1;
        }
        if(start >= end) {
            return new ArrayList<>();
        }

        final List<SurveyPeriodAvgMinMax> result = new ArrayList<>();
        Long frequencyId = getFrequencyIdNotDeletedOrThrow(deviceId, frequencyMhz);
        List<DeviceSurvey> beforeList = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE " +
                        "ds.frequency.id = :fi AND ds.timestamp <= :t AND ds.deleted = 0 " +
                        "ORDER BY ds.timestamp DESC",
                DeviceSurvey.class)
                .setParameter("fi", frequencyId)
                .setParameter("t", start)
                .setMaxResults(1)
                .getResultList();
        int timeBegin;
        if(beforeList.isEmpty()) {
            timeBegin = start;
        }
        else {
            timeBegin = beforeList.get(0).getTimestamp();
        }
        ArrayList<DeviceSurvey> surveys;
        final List<DeviceSurvey> originalSurveys = deviceSurveyRepository.findFromPeriodNotDeletedOrderByTime(frequencyId, timeBegin, end);
        if(originalSurveys instanceof ArrayList) {
            surveys = (ArrayList<DeviceSurvey>)originalSurveys;
        }
        else {
            surveys = new ArrayList<>();
            for(DeviceSurvey ds : originalSurveys) {
                surveys.add(ds);
            }
        }

        if(surveys.isEmpty()) {
            return result;
        }
        final DeviceSurvey first = surveys.get(0);
        int t0, t1, processed;
        if(first.getTimestamp() < start) {
            t0 = start;
            processed = 1;
        }
        else {
            t0 = first.getTimestamp();
            processed = 0;
        }
        t1 = t0 + groupTime;
        while(t0 < end && 0 < t0 && t0 < t1) {
            if(end < t1)t1 = end;
            int min, max, span, cumulativeEndTime, cumulativeEndClients;
            long cumulative = 0L;
            DeviceSurvey begin;
            if(processed < surveys.size()) {
                begin = surveys.get(processed);
                if( begin.getTimestamp() > t0 ) {
                    begin = surveys.get(--processed);
                }
            }
            else {
                begin = surveys.get(--processed);
            }
            min = begin.getClientsSum();
            max = begin.getClientsSum();
            span = 1;
            cumulativeEndTime = t0;
            cumulativeEndClients = begin.getClientsSum();
            processed++;
            while(processed < surveys.size()) {
                final DeviceSurvey next = surveys.get(processed);
                if(next.getTimestamp() < t1) {
                    min = Integer.min( min, next.getClientsSum() );
                    max = Integer.max( max, next.getClientsSum() );
                    final long duration = next.getTimestamp() - cumulativeEndTime;
                    cumulative += duration * cumulativeEndClients;
                    cumulativeEndTime = next.getTimestamp();
                    cumulativeEndClients = next.getClientsSum();
                    span++;
                    processed++;
                }
                else {
                    break;
                }
            }
            final long duration = t1 - cumulativeEndTime;
            cumulative += duration * cumulativeEndClients;
            double avg = (double)cumulative / (t1 - t0);
            SurveyPeriodAvgMinMax elem = new SurveyPeriodAvgMinMax();
            elem.setTimeStart(t0);
            elem.setTimeEnd(t1);
            elem.setMin(min);
            elem.setMax(max);
            elem.setAverage(avg);
            elem.setSurveySpan(span);
            result.add(elem);
            t0 = t1;
            t1 = t0 + groupTime;
        }
        return result;
    }
}