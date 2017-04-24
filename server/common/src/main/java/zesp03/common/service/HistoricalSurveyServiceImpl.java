package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.ShortSurvey;
import zesp03.common.data.SurveyPeriodAvgMinMax;
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
        List<DeviceSurvey> beforeList = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE " +
                        "ds.frequency.id = :fi AND ds.timestamp <= :t " +
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
        log.debug("surveys between {} and {}: {}", timeBegin, end, surveys.size());
        return result;
    }
}