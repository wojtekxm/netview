package zesp03.common.service;

import zesp03.common.data.DeviceNow;
import zesp03.common.data.ExamineResult;
import zesp03.common.data.MinmaxSurveyData;
import zesp03.common.entity.DeviceSurvey;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

public interface SurveyService {
    /**
     * Zwraca listę badań dla urządzenia o id równym <code>device</code>.
     * Zwrócona lista będzie posortowana po czasie wykonania badania, rosnąco.
     * Lista może być pusta.
     * Lista będzie zawierać tylko badania których czas spełnia warunek: start <= czas < end.
     *
     * @param start  timestamp w sekundach, dolny limit (włącznie) czasu badań
     * @param end    timestamp w sekundach, górny limit (wyłącznie) czasu badań.
     *               Musi być większy od <code>start</code>.
     */
    List<DeviceSurvey> getOriginalSurveys(long deviceId, int start, int end);

    /**
     * @param start timestamp w sekundach, dolny limit (włącznie) czasu badań
     * @param end timestamp w sekundach, górny limit (wyłącznie) czasu badań.
     *            Musi być większy od <code>start</code>.
     * @return Średnia liczba klientów dla danego urządzenia w wybranym okresie czasu.
     */
    double getAverageSurvey(long deviceId, int start, int end);

    /**
     * @param start timestamp w sekundach, dolny limit (włącznie) czasu badań
     * @param end timestamp w sekundach, górny limit (wyłącznie) czasu badań.
     *            Musi być większy od <code>start</code>.
     */
    MinmaxSurveyData getMinmaxSimple(long deviceId, int start, int end);

    List<DeviceNow> checkAll();

    List<DeviceNow> checkSome(Collection<Long> ids);

    DeviceNow checkOne(Long id);

    List<DeviceNow> checkAll(EntityManager em);

    List<DeviceNow> checkSome(Collection<Long> ids, EntityManager em);

    DeviceNow checkOne(Long id, EntityManager em);

    ExamineResult examineAll();

    /**
     * Wykonuje badanie wskazanego kontrolera.
     * @param controllerId id kontrolera
     * @return liczba zaktualizowanych urządzeń (liczba nowych zapisanych badań)
     */
    ExamineResult examineOne(long controllerId);
}
