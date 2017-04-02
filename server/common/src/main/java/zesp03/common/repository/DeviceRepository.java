package zesp03.common.repository;

import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.Device;

public interface DeviceRepository extends CrudRepository<Device, Long> {
}
