package zesp03.common.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;

import java.util.List;

public interface DeviceSurveyRepository extends CrudRepository<DeviceSurvey, Long> {
    @Query(value = "SELECT ds.id FROM device_survey ds INNER JOIN (SELECT frequency_id, MAX(`timestamp`) AS m FROM device_survey GROUP BY frequency_id) best ON ds.frequency_id = best.frequency_id AND ds.`timestamp` = best.m", nativeQuery = true)
    List<Number> findLastIds();

    @Query("SELECT ds FROM DeviceSurvey ds WHERE ds.frequency.id = ?1 ORDER BY ds.timestamp ASC")
    List<DeviceSurvey> findFirst(Long frequencyId);

    @Query("SELECT ds FROM DeviceSurvey ds WHERE ds.frequency.id = ?1 ORDER BY ds.timestamp DESC")
    List<DeviceSurvey> findLast(Long frequencyId);

    @Query("SELECT ds FROM DeviceSurvey ds WHERE ds.frequency.id = ?1 AND ds.timestamp >= ?2 AND ds.timestamp < ?3 ORDER BY ds.timestamp ASC")
    List<DeviceSurvey> findFromPeriodOrderByTime(Long frequencyId, Integer timeStart, Integer timeEnd);

    @Query("SELECT ds FROM DeviceSurvey ds WHERE ds.frequency.id = ?1 AND ds.timestamp <= ?2 ORDER BY ds.timestamp DESC")
    List<DeviceSurvey> findLastNotAfter(Long frequencyId, Integer timeLast);

    @Modifying
    @Query("DELETE FROM DeviceSurvey ds WHERE ds.frequency = ?1")
    void deleteByFrequency(DeviceFrequency df);
}
