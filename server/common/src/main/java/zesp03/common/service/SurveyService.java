package zesp03.common.service;

import zesp03.common.data.DeviceNow;
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
    List<DeviceSurvey> getOriginal(long deviceId, int start, int end);

    /**
     * @param start timestamp w sekundach, dolny limit (włącznie) czasu badań
     * @param end timestamp w sekundach, górny limit (wyłącznie) czasu badań.
     *            Musi być większy od <code>start</code>.
     * @return Średnia liczba klientów dla danego urządzenia w wybranym okresie czasu.
     */
    double getAverage(long deviceId, int start, int end);

    /**
     * @param start timestamp w sekundach, >= 0
     * @param groups liczba grup, >= 0
     * @param groupTime czas trwania jednej grupy w sekundach, > 0
     */
    List<Double> getMultiAverage(long deviceId, int start, int groups, int groupTime);

    /**
     * @param start timestamp w sekundach, dolny limit (włącznie) czasu badań
     * @param end timestamp w sekundach, górny limit (wyłącznie) czasu badań.
     *            Musi być większy od <code>start</code>.
     */
    MinmaxSurveyData getMinmax(long deviceId, int start, int end);

    /**
     * @param start timestamp w sekundach, >= 0
     * @param groups liczba grup, >= 0
     * @param groupTime czas trwania jednej grupy w sekundach, > 0
     */
    List<MinmaxSurveyData> getMultiMinmax(long deviceId, int start, int groups, int groupTime);

    List<DeviceNow> checkAll();

    List<DeviceNow> checkSome(Collection<Long> ids);

    DeviceNow checkOne(Long id);

    List<DeviceNow> checkAll(EntityManager em);

    List<DeviceNow> checkSome(Collection<Long> ids, EntityManager em);

    DeviceNow checkOne(Long id, EntityManager em);

    /**
     * @return liczba zaktualizowanych urządzeń
     */
    int examineAll();

    /**
     * Wykonuje badanie wskazanego kontrolera.
     * @param controllerId id kontrolera
     * @return liczba zaktualizowanych urządzeń (liczba nowych zapisanych badań)
     */
    int examineOne(long controllerId);
}
