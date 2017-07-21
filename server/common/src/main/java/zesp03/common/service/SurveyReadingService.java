package zesp03.common.service;

import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.RangeSamples;
import zesp03.common.data.SampleAvgMinMax;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SurveyReadingService {
    Long getFrequencyIdNotDeletedOrThrow(Long deviceId, Integer frequencyMhz);

    // kluczem w mapie jest id urządzenia
    Map<Long, CurrentDeviceState> checkAllFetch();

    // kluczem w mapie jest id urządzenia
    Map<Long, CurrentDeviceState> checkAllNotInBuildingFetch(Long buildingId);

    // kluczem w mapie jest id urządzenia
    Map<Long, CurrentDeviceState> checkForControllerFetch(Long controllerId);

    // kluczem w mapie jest id urządzenia
    Map<Long, CurrentDeviceState> checkForBuildingFetch(Long buildingId);

    Optional<CurrentDeviceState> checkOneFetch(Long deviceId);

    long countAllForAll();

    long countBeforeForAll(int before);

    long countAllForOne(Long deviceId);

    long countBeforeForOne(Long deviceId, int before);

    /**
     * Zwraca listę badań dla urządzenia o id równym <code>device</code>.
     * Zwrócona lista będzie posortowana po czasie wykonania badania, rosnąco.
     * Lista może być pusta.
     *
     * @param start  timestamp w sekundach, dolny limit (włącznie) czasu badań
     * @param end    timestamp w sekundach, górny limit (wyłącznie) czasu badań.
     *               Musi być większy od <code>start</code>.
     */
    RangeSamples getOriginal(long deviceId, int frequencyMhz, int start, int end);

    /**
     * @param start timestamp w sekundach, nieujemny
     * @param end timestamp w sekundach, większy niż start
     * @param groupTime czas trwania jednej grupy w sekundach, dodatnie
     */
    List<SampleAvgMinMax> getMultiAvgMinMax(long deviceId, int frequencyMhz, int start, int end, int groupTime);
}
