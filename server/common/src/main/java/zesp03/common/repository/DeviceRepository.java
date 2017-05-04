package zesp03.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends CrudRepository<Device, Long> {
    @Query("SELECT d FROM Device d WHERE d.deleted = 0")
    List<Device> findAllNotDeleted();

    @Query("SELECT d FROM Device d LEFT JOIN FETCH d.controller LEFT JOIN FETCH d.building WHERE d.deleted = 0")
    List<Device> findAllFetchNotDeleted();

    @Query("SELECT d FROM Device d WHERE d.id = ?1 AND d.deleted = 0")
    Optional<Device> findOneNotDeleted(Long deviceId);

    @Query("SELECT d FROM Device d LEFT JOIN FETCH d.controller LEFT JOIN FETCH d.building WHERE d.id = ?1 AND d.deleted = 0")
    Optional<Device> findOneFetchNotDeleted(Long deviceId);
}
