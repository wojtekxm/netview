package zesp03.common.service;

import zesp03.common.data.CurrentDeviceState;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DeviceService {
    List<Long> findLastIds();
    Optional<CurrentDeviceState> checkOne(Long deviceId);
    // kluczem w mapie jest id urządzenia
    Map<Long, CurrentDeviceState> checkSome(Collection<Long> deviceIds);
    // kluczem w mapie jest id urządzenia
    Map<Long, CurrentDeviceState> checkAll();
}
