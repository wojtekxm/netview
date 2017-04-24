package zesp03.common.repository;

import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.ViewLastSurvey;

/**
 * Only for selects, because ViewLastSurvey is @Immutable!
 */
public interface ViewLastSurveyRepository extends CrudRepository<ViewLastSurvey, Long> {
}
