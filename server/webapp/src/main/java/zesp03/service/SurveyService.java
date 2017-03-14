package zesp03.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.common.Database;
import zesp03.common.NotFoundException;
import zesp03.dto.AverageSurveyDto;
import zesp03.dto.OriginalSurveyDto;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

public class SurveyService {
    private static final Logger log = LoggerFactory.getLogger(SurveyService.class);

    /**
     * Zwraca listę badań dla urządzenia o id równym <code>device</code>.
     * Zwrócona lista będzie posortowana po czasie wykonania badania, rosnąco.
     * Lista może być pusta.
     * Lista będzie zawierać tylko badania których czas spełnia warunek: start <= czas < end.
     *
     * @param deviceId id urządzenia
     * @param start  timestamp w sekundach, dolny limit (włącznie) czasu badań
     * @param end    timestamp w sekundach, górny limit (wyłącznie) czasu badań
     */
    public List<OriginalSurveyDto> getOriginalSurveys(long deviceId, int start, int end)
            throws NotFoundException {
        List<OriginalSurveyDto> result;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Device d = em.find(Device.class, deviceId);
            if (d == null)
                throw new NotFoundException("no such device");

            result = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE ds.device = :device AND ds.timestamp >= :start AND ds.timestamp < :end ORDER BY ds.timestamp ASC",
                    DeviceSurvey.class)
                    .setParameter("device", d)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList()
                    .stream()
                    .map(ds -> {
                        OriginalSurveyDto dto = new OriginalSurveyDto();
                        dto.setTimestamp(ds.getTimestamp());
                        dto.setClientsSum(ds.getClientsSum());
                        return dto;
                    })
                    .collect(Collectors.toList());

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        return result;
    }

    /**
     * @param deviceId I urządzenia
     * @param start Timestamp w sekundach, początkowy czas do wyliczenia średniej (włącznie)
     * @param end Timestamp w sekundach, końcowy czas okresu do wyliczenia średniej (wyłącznie).
     *            Musi być większy od <code>start</code>.
     * @return Średnia liczba klientów dla danego urządzenia w wybranym okresie czasu.
     * @throws NotFoundException nie ma takiego urządzenia
     */
    public AverageSurveyDto getAverageSurvey(long deviceId, int start, int end)
            throws NotFoundException {
        if(start < 0)
            throw new IllegalArgumentException("start < 0");
        if(end < 0)
            throw new IllegalArgumentException("end < 0");
        if(start >= end)
            throw new IllegalArgumentException("start >= end");

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Device device = em.find(Device.class, deviceId);
            if(device == null)
                throw new NotFoundException("no such device");

            List<DeviceSurvey> listEnd = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE ds.device = :device AND " +
                    "ds.timestamp <= :time ORDER BY ds.timestamp DESC ", DeviceSurvey.class)
                    .setParameter("device", device)
                    .setParameter("time", end)
                    .setMaxResults(1)
                    .getResultList();
            if(listEnd.isEmpty()) {
                tran.rollback();
                AverageSurveyDto result = new AverageSurveyDto();
                result.setAvgClients(0.0);
                return result;
            }
            DeviceSurvey surEnd = listEnd.get(0);
            long cumulativeEnd = surEnd.getCumulative() + (long)surEnd.getClientsSum() * ( end - surEnd.getTimestamp() );
            log.debug("cumulativeEnd = {}", cumulativeEnd);

            List<DeviceSurvey> listStart = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE ds.device = :device AND " +
                    "ds.timestamp <= :time ORDER BY ds.timestamp DESC ", DeviceSurvey.class)
                    .setParameter("device", device)
                    .setParameter("time", start)
                    .setMaxResults(1)
                    .getResultList();
            long cumulativeStart;
            if(listStart.isEmpty()) {
                cumulativeStart = 0;
            }
            else {
                DeviceSurvey surStart = listStart.get(0);
                cumulativeStart = surStart.getCumulative() + (long)surStart.getClientsSum() * ( start - surStart.getTimestamp() );
            }
            log.debug("cumulativeStart = {}", cumulativeStart);

            long num = cumulativeEnd - cumulativeStart;
            AverageSurveyDto result = new AverageSurveyDto();
            result.setAvgClients( (double)num / (end - start) );

            tran.commit();
            return result;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }
}