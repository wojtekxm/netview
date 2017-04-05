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
import zesp03.common.repository.DeviceSurveyRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        return deviceSurveyRepository.findFromPeriod(frequencyId, start, end)
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
}