package zesp03.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.DeviceFrequency;

import java.util.Optional;

public interface DeviceFrequencyRepository extends CrudRepository<DeviceFrequency, Long> {
    @Query("SELECT df FROM DeviceFrequency df WHERE df.device.id = ?1 AND df.frequency = ?2")
    Optional<DeviceFrequency> findByDeviceAndFrequency(Long deviceId, Integer frequencyMhz);
}
