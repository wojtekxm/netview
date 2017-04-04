package zesp03.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.exception.NotFoundException;
import zesp03.common.service.DeviceService;
import zesp03.webapp.dto.CurrentDeviceStateDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommonServiceWrapperImpl implements CommonServiceWrapper {
    @Autowired
    private DeviceService deviceService;

    @Override
    public CurrentDeviceStateDto checkOneDevice(Long deviceId) {
        Optional<CurrentDeviceState> opt = deviceService.checkOne(deviceId);
        if( ! opt.isPresent() ) {
            throw new NotFoundException("device");
        }
        return CurrentDeviceStateDto.make( opt.get() );
    }

    @Override
    public List<CurrentDeviceStateDto> checkAllDevices() {
        return deviceService.checkAll()
                .values()
                .stream()
                .map(CurrentDeviceStateDto::make)
                .collect(Collectors.toList());
    }
}
