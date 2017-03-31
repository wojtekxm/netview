package zesp03.common.service;

import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.MinmaxSurveyData;
import zesp03.common.entity.DeviceSurvey;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    List<DeviceSurvey> getOriginal(long deviceId, int frequencyMhz, int start, int end);

    /**
     * @param start timestamp w sekundach, dolny limit (włącznie) czasu badań
     * @param end timestamp w sekundach, górny limit (wyłącznie) czasu badań.
     *            Musi być większy od <code>start</code>.
     * @return Średnia liczba klientów dla danego urządzenia w wybranym okresie czasu.
     */
    double getAverage(long deviceId, int frequencyMhz, int start, int end);

    /**
     * @param start timestamp w sekundach, >= 0
     * @param groups liczba grup, >= 0
     * @param groupTime czas trwania jednej grupy w sekundach, > 0
     */
    List<Double> getMultiAverage(long deviceId, int frequencyMhz, int start, int groups, int groupTime);

    /**
     * @param start timestamp w sekundach, dolny limit (włącznie) czasu badań
     * @param end timestamp w sekundach, górny limit (wyłącznie) czasu badań.
     *            Musi być większy od <code>start</code>.
     */
    MinmaxSurveyData getMinmax(long deviceId, int frequencyMhz, int start, int end);

    /**
     * @param start timestamp w sekundach, >= 0
     * @param groups liczba grup, >= 0
     * @param groupTime czas trwania jednej grupy w sekundach, > 0
     */
    List<MinmaxSurveyData> getMultiMinmax(long deviceId, int frequencyMhz, int start, int groups, int groupTime);

    Map<Long, CurrentDeviceState> xAll();

    Map<Long, CurrentDeviceState> xSome(Collection<Long> devices);

    CurrentDeviceState xOne(Long deviceId);
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
