/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.repository;

import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.ViewLastSurvey;

/**
 * Only for selects, because ViewLastSurvey is @Immutable!
 */
public interface ViewLastSurveyRepository extends CrudRepository<ViewLastSurvey, Long> {
}
