/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.repository;

import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.Building;

public interface BuildingRepository extends CrudRepository<Building, Long> {
}
