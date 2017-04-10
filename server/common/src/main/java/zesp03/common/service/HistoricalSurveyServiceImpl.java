package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.ShortSurvey;
import zesp03.common.data.SurveyPeriodAvg;
import zesp03.common.data.SurveyPeriodAvgMinMax;
import zesp03.common.data.SurveyPeriodMinMax;
import zesp03.common.entity.DeviceSurvey;
import zesp03.common.exception.ValidationException;
import zesp03.common.repository.DeviceSurveyRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HistoricalSurveyServiceImpl implements HistoricalSurveyService {
    private static final Logger log = LoggerFactory.getLogger(HistoricalSurveyServiceImpl.class);

    @Autowired
    private DeviceSurveyRepository deviceSurveyRepository;

    @Autowired
    private DeviceFrequencyService deviceFrequencyService;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ShortSurvey> getOriginal(long deviceId, int frequencyMhz, int start, int end) {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");

        Long frequencyId = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        return deviceSurveyRepository.findFromPeriodOrderByTime(frequencyId, start, end)
                .stream()
                .map(ShortSurvey::make)
                .collect(Collectors.toList());
    }

    @Override
    public SurveyPeriodAvg getAverage(long deviceId, int frequencyMhz, int start, int end) {
        Long frequencyId = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        return getAverage(frequencyId, start, end);
    }

    @Override
    public SurveyPeriodAvgMinMax getAvgMinMax(long deviceId, int frequencyMhz, int start, int end) {
        Long frequencyId = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        return getAvgMinMax(frequencyId, start, end);
    }

    @Override
    public SurveyPeriodMinMax getMinMax(long deviceId, int frequencyMhz, int start, int end) {
        Long frequencyId = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        return getMinMax(frequencyId, start, end);
    }

    private SurveyPeriodAvg getAverage(Long frequencyId, int start, int end) {
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

    private SurveyPeriodMinMax getMinMax(Long frequencyId, int start, int end) {
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

    private SurveyPeriodAvgMinMax getAvgMinMax(Long frequencyId, int start, int end) {
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
    public List<SurveyPeriodAvgMinMax> getMultiAvgMinMax_Slow(long deviceId, int frequencyMhz, int start, int end, int groupTime) {
        if(start < 0) {
            throw new ValidationException("start", "less than 0");
        }
        if(start >= end) {
            throw new ValidationException("start", "greater or equal end");
        }
        if(groupTime < 1) {
            throw new ValidationException("groupTime", "less than 1");
        }
        final int now = (int)Instant.now().getEpochSecond();
        if(end > now)end = now + 1;

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
        Long frequencyId = deviceFrequencyService.getFrequencyIdOrThrow(deviceId, frequencyMhz);
        List<DeviceSurvey> beforeList = deviceSurveyRepository.findLastNotAfter(frequencyId, start);
        int timeBegin;
        if(beforeList.isEmpty()) {
            timeBegin = start;
        }
        else {
            timeBegin = beforeList.get(0).getTimestamp();
        }
        ArrayList<DeviceSurvey> surveys;
        final List<DeviceSurvey> originalSurveys = deviceSurveyRepository.findFromPeriodOrderByTime(frequencyId, timeBegin, end);
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