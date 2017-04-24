package zesp03.common.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;

import java.util.List;

public interface DeviceSurveyRepository extends CrudRepository<DeviceSurvey, Long> {
    @Query("SELECT ds FROM DeviceSurvey ds WHERE ds.frequency.id = ?1 AND ds.timestamp >= ?2 AND ds.timestamp < ?3 ORDER BY ds.timestamp ASC")
    List<DeviceSurvey> findFromPeriodOrderByTime(Long frequencyId, Integer timeStart, Integer timeEnd);

    @Modifying
    @Query("DELETE FROM DeviceSurvey ds WHERE ds.frequency = ?1")
    void deleteByFrequency(DeviceFrequency df);
}
