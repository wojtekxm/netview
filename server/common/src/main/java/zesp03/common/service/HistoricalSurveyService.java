package zesp03.common.service;

import zesp03.common.data.ShortSurvey;
import zesp03.common.data.SurveyPeriodAvgMinMax;

import java.util.List;

public interface HistoricalSurveyService {
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
     * @param start timestamp w sekundach, >= 0
     * @param end timestamp w sekundach, end > start
     * @param groupTime czas trwania jednej grupy w sekundach, > 0
     */
    List<SurveyPeriodAvgMinMax> getMultiAvgMinMax(long deviceId, int frequencyMhz, int start, int end, int groupTime);
}
