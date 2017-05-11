package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.RangeSamples;
import zesp03.common.data.SampleAvgMinMax;
import zesp03.common.data.SampleRaw;
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
    public Map<Long, CurrentDeviceState> checkAllFetch() {
        HashMap<Long, CurrentDeviceState> map = new HashMap<>();
        List<Object[]> list = em.createQuery(
                selectCheck(""),
                Object[].class)
                .getResultList();
        for(Object[] arr : list) {
            merge(map, arr);
        }
        return map;
    }

    @Override
    public Map<Long, CurrentDeviceState> checkForControllerFetch(Long controllerId) {
        HashMap<Long, CurrentDeviceState> map = new HashMap<>();
        List<Object[]> list = em.createQuery(
                selectCheck("dev.controller.id = :cid AND"),
                Object[].class)
                .setParameter("cid", controllerId)
                .getResultList();
        for(Object[] arr : list) {
            merge(map, arr);
        }
        return map;
    }

    @Override
    public Map<Long, CurrentDeviceState> checkForBuildingFetch(Long buildingId) {
        HashMap<Long, CurrentDeviceState> map = new HashMap<>();
        List<Object[]> list = em.createQuery(
                selectCheck("dev.building.id = :bid AND"),
                Object[].class)
                .setParameter("bid", buildingId)
                .getResultList();
        for(Object[] arr : list) {
            merge(map, arr);
        }
        return map;
    }

    @Override
    public Optional<CurrentDeviceState> checkOneFetch(Long deviceId) {
        CurrentDeviceState result = null;
        List<Object[]> list = em.createQuery(
                selectCheck("dev.id = :deviceId AND"),
                Object[].class)
                .setParameter("deviceId", deviceId)
                .getResultList();
        for(Object[] arr : list) {
            result = merge(result, arr);
        }
        if(result != null) {
            return Optional.of(result);
        }
        else {
            return Optional.empty();
        }
    }

    private String selectCheck(String filter) {
        return "SELECT dev, df, sur FROM Device dev " +
                "LEFT JOIN FETCH dev.controller con " +
                "LEFT JOIN FETCH dev.building bui " +
                "LEFT JOIN DeviceFrequency df ON dev.id = df.device.id " +
                "LEFT JOIN ViewLastSurvey vfs ON df.id = vfs.frequencyId " +
                "LEFT JOIN DeviceSurvey sur ON vfs.surveyId = sur.id WHERE " +
                filter +
                " (dev.deleted = 0 OR dev.deleted IS NULL) AND " +
                "( df.deleted = 0 OR  df.deleted IS NULL) AND " +
                "(sur.deleted = 0 OR sur.deleted IS NULL)";
    }

    private CurrentDeviceState merge(CurrentDeviceState currentOrNull, Object[] arrDevFreqSur) {
        final Device dev = (Device)arrDevFreqSur[0];
        final DeviceFrequency df = (DeviceFrequency)arrDevFreqSur[1];
        final DeviceSurvey sur = (DeviceSurvey)arrDevFreqSur[2];
        final CurrentDeviceState next = new CurrentDeviceState(dev, df, sur);
        if(currentOrNull != null) {
            currentOrNull.merge(next);
            return currentOrNull;
        }
        else {
            return next;
        }
    }

    private void merge(Map<Long, CurrentDeviceState> map, Object[] arrDevFreqSur) {
        Long deviceId = ((Device)arrDevFreqSur[0]).getId();
        CurrentDeviceState current = map.get(deviceId);
        CurrentDeviceState result = merge(current, arrDevFreqSur);
        map.put(deviceId, result);
    }

    @Override
    public RangeSamples getOriginal(long deviceId, int frequencyMhz, int start, int end) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");

        final Long frequencyId = getFrequencyIdNotDeletedOrThrow(deviceId, frequencyMhz);
        final List<SampleRaw> list = deviceSurveyRepository
                .findFromPeriodNotDeletedOrderByTime(frequencyId, start, end)
                .stream()
                .map(SampleRaw::make)
                .collect(Collectors.toList());
        final List<DeviceSurvey> beforeList = em.createQuery("SELECT ds FROM DeviceSurvey ds " +
                        "WHERE ds.frequency.id = :fid AND ds.deleted = 0 AND " +
                        "ds.timestamp < :t ORDER BY ds.timestamp DESC",
                DeviceSurvey.class)
                .setParameter("fid", frequencyId)
                .setParameter("t", start)
                .setMaxResults(1)
                .getResultList();
        SampleRaw before = null;
        if(! beforeList.isEmpty()) {
            before = SampleRaw.make( beforeList.get(0) );
        }
        final List<DeviceSurvey> afterList = em.createQuery("SELECT ds FROM DeviceSurvey ds " +
                        "WHERE ds.frequency.id = :fid AND ds.deleted = 0 AND " +
                        "ds.timestamp >= :end ORDER BY ds.timestamp ASC",
                DeviceSurvey.class)
                .setParameter("fid", frequencyId)
                .setParameter("end", end)
                .setMaxResults(1)
                .getResultList();
        SampleRaw after = null;
        if(! afterList.isEmpty()) {
            after = SampleRaw.make( afterList.get(0) );
        }
        final RangeSamples ranged = new RangeSamples();
        ranged.setBefore(before);
        ranged.setList(list);
        ranged.setAfter(after);
        return ranged;
    }

    @Override
    public List<SampleAvgMinMax> getMultiAvgMinMax(final long deviceId, final int frequencyMhz, final int start, int end, final int groupTime) {
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

        final List<SampleAvgMinMax> mainList = new ArrayList<>();
        if(start >= end) {
            return mainList;
        }

        final Long frequencyId = getFrequencyIdNotDeletedOrThrow(deviceId, frequencyMhz);
        final List<DeviceSurvey> beforeList = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE " +
                        "ds.frequency.id = :fid AND ds.deleted = 0 AND ds.timestamp <= :t " +
                        "ORDER BY ds.timestamp DESC",
                DeviceSurvey.class)
                .setParameter("fid", frequencyId)
                .setParameter("t", start)
                .setMaxResults(2)
                .getResultList();
        int timeBegin;
        if(beforeList.isEmpty()) {
            timeBegin = start;
        }
        else {
            final DeviceSurvey survey = beforeList.get(0);
            timeBegin = survey.getTimestamp();
            if(survey.getTimestamp() < start) {
                //ranged.setBefore(SampleRaw.make(survey));
            }
            else if(beforeList.size() > 1) {
                final DeviceSurvey next = beforeList.get(1);
                //ranged.setBefore(SampleRaw.make(next));
            }
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
            return mainList;
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
            SampleAvgMinMax elem = new SampleAvgMinMax();
            elem.setTimeStart(t0);
            elem.setTimeEnd(t1);
            elem.setMin(min);
            elem.setMax(max);
            elem.setAverage(avg);
            elem.setSurveySpan(span);
            mainList.add(elem);
            t0 = t1;
            t1 = t0 + groupTime;
        }
        return mainList;
    }
}