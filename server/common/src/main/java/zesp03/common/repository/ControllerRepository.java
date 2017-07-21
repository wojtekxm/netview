/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.Controller;

import java.util.List;
import java.util.Optional;

public interface ControllerRepository extends CrudRepository<Controller, Long> {
    @Query("SELECT c FROM Controller c WHERE c.id = ?1 AND c.deleted = 0")
    Optional<Controller> findOneNotDeleted(Long controllerId);

    @Query("SELECT c FROM Controller c WHERE c.deleted = 0")
    List<Controller> findAllNotDeleted();

    @Query("SELECT COUNT(c.id) FROM Controller c WHERE c.building.id = ?1")
    Number countByBuilding(Long buildingId);
}
