package zesp03.common.repository;

import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.Building;

public interface BuildingRepository extends CrudRepository<Building, Long> {
}
