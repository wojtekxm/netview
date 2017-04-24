package zesp03.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends CrudRepository<Device, Long> {
    @Query("SELECT d FROM Device d WHERE d.id = ?1 AND d.deleted = FALSE")
    Optional<Device> findOneNotDeleted(Long deviceId);

    @Query("SELECT d FROM Device d WHERE d.deleted = FALSE")
    List<Device> findAllNotDeleted();
}
