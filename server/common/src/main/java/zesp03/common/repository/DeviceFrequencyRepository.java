/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.DeviceFrequency;

import java.util.List;
import java.util.Optional;

public interface DeviceFrequencyRepository extends CrudRepository<DeviceFrequency, Long> {
    @Query("SELECT f FROM DeviceFrequency f WHERE f.id = ?1 AND f.deleted = 0")
    Optional<DeviceFrequency> findOneNotDeleted(Long frequencyId);

    @Query("SELECT f FROM DeviceFrequency f WHERE f.deleted = 0")
    List<DeviceFrequency> findAllNotDeleted();

    @Query("SELECT f FROM DeviceFrequency f WHERE f.device.id = ?1 AND f.frequency = ?2 AND f.deleted = 0")
    Optional<DeviceFrequency> findByDeviceAndFrequencyNotDeleted(Long deviceId, Integer frequencyMhz);
}
