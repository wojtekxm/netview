package zesp03.common.service;

import zesp03.common.data.*;

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
    List<ShortSurvey> getOriginal(long deviceId, int frequencyMhz, int start, int end);

    /**
     * @param start timestamp w sekundach, dolny limit (włącznie) czasu badań, start >= 0.
     * @param end timestamp w sekundach, górny limit (wyłącznie) czasu badań, end > start.
     * @return Zwraca null jeśli nie będzie żadnych badań na podstawie których można wyliczyć średnią.
     */
    SurveyPeriodAvg getAverage(long deviceId, int frequencyMhz, int start, int end);

    /**
     * @param start timestamp w sekundach, dolny limit (włącznie) czasu badań, start >= 0.
     * @param end timestamp w sekundach, górny limit (wyłącznie) czasu badań, end > start.
     * @return Zwraca null jeśli nie będzie żadnych badań na podstawie których można wyliczyć min-max.
     */
    SurveyPeriodMinMax getMinMax(long deviceId, int frequencyMhz, int start, int end);

    /**
     * @param start timestamp w sekundach, dolny limit (włącznie) czasu badań, start >= 0.
     * @param end timestamp w sekundach, górny limit (wyłącznie) czasu badań, end > start.
     * @return Zwraca null jeśli nie będzie żadnych badań na podstawie których można wyliczyć avg i min-max.
     */
    SurveyPeriodAvgMinMax getAvgMinMax(long deviceId, int frequencyMhz, int start, int end);

    /**
     * @param start timestamp w sekundach, start >= 0
     * @param end timestamp w sekundach, end > start
     * @param groupTime czas trwania jednej grupy w sekundach, groupTime > 0
     */
    List<SurveyPeriodAvg> getMultiAverage(long deviceId, int frequencyMhz, int start, int end, int groupTime);

    /**
     * @param start timestamp w sekundach, >= 0
     * @param end timestamp w sekundach, end > start
     * @param groupTime czas trwania jednej grupy w sekundach, > 0
     */
    List<SurveyPeriodMinMax> getMultiMinMax(long deviceId, int frequencyMhz, int start, int end, int groupTime);

    /**
     * @param start timestamp w sekundach, >= 0
     * @param end timestamp w sekundach, end > start
     * @param groupTime czas trwania jednej grupy w sekundach, > 0
     */
    List<SurveyPeriodAvgMinMax> getMultiAvgMinMax(long deviceId, int frequencyMhz, int start, int end, int groupTime);

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
