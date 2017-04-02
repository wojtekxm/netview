package zesp03.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.exception.NotFoundException;
import zesp03.common.repository.DeviceFrequencyRepository;

import java.util.Optional;

@Service
@Transactional
public class DeviceFrequencyServiceImpl implements DeviceFrequencyService {
    @Autowired
    DeviceFrequencyRepository deviceFrequencyRepository;

    @Override
    public Long getFrequencyIdOrThrow(Long deviceId, Integer frequencyMhz) {
        Optional<DeviceFrequency> opt = deviceFrequencyRepository.findByDeviceAndFrequency(deviceId, frequencyMhz);
        if( ! opt.isPresent() ) {
            throw new NotFoundException("device with this id and frequency");
        }
        return opt.get().getId();
    }
}
