/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.DeviceSurvey;

import java.util.Collection;
import java.util.List;

public interface DeviceSurveyRepository extends CrudRepository<DeviceSurvey, Long> {
    @Query("SELECT ds FROM DeviceSurvey ds WHERE ds.frequency.id = ?1 AND ds.timestamp >= ?2 AND ds.timestamp < ?3 ORDER BY ds.timestamp ASC")
    List<DeviceSurvey> findFromPeriodOrderByTime(Long frequencyId, Integer timeStart, Integer timeEnd);

    @Query("SELECT COUNT(ds.id) FROM DeviceSurvey ds WHERE ds.frequency.id IN ?1")
    Long countForDeviceFrequencies(Collection<Long> deviceFrequencyIds);

    @Query("SELECT COUNT(ds.id) FROM DeviceSurvey ds WHERE ds.frequency.id IN ?1 AND ds.timestamp < ?2")
    Long countBeforeForDeviceFrequencies(Collection<Long> deviceFrequencyIds, int before);
}
