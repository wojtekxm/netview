package zesp03.common.service;

import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.RangeSamples;
import zesp03.common.data.SampleAvgMinMax;
import zesp03.common.data.SampleRaw;
import zesp03.common.entity.Controller;

import java.util.Map;
import java.util.Optional;

public interface SurveyReadingService {
    Long getFrequencyIdNotDeletedOrThrow(Long deviceId, Integer frequencyMhz);

    Optional<CurrentDeviceState> checkOne(Long deviceId);
    // kluczem w mapie jest id urządzenia
    Map<Long, CurrentDeviceState> checkForController(Controller c);
    // kluczem w mapie jest id urządzenia
    Map<Long, CurrentDeviceState> checkAllFetch();

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
    RangeSamples<SampleRaw> getOriginal(long deviceId, int frequencyMhz, int start, int end);

    /**
     * @param start timestamp w sekundach, >= 0
     * @param end timestamp w sekundach, end > start
     * @param groupTime czas trwania jednej grupy w sekundach, > 0
     */
    RangeSamples<SampleAvgMinMax> getMultiAvgMinMax(long deviceId, int frequencyMhz, int start, int end, int groupTime);
}
