package zesp03.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.Controller;

import java.util.List;
import java.util.Optional;

public interface ControllerRepository extends CrudRepository<Controller, Long> {
    @Query("SELECT c FROM Controller c WHERE c.id = ?1 AND c.deleted = FALSE")
    Optional<Controller> findOneNotDeleted(Long controllerId);

    @Query("SELECT c FROM Controller c WHERE c.deleted = FALSE")
    List<Controller> findAllNotDeleted();
}
