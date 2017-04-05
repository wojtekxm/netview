package zesp03.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.entity.Device;
import zesp03.common.exception.NotFoundException;
import zesp03.common.repository.DeviceRepository;
import zesp03.webapp.dto.DeviceDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public DeviceDto getOne(Long deviceId) {
        Device d = deviceRepository.findOne(deviceId);
        if(d == null) {
            throw new NotFoundException("device");
        }
        return DeviceDto.make(d);
    }

    @Override
    public List<DeviceDto> getAll() {
        List<DeviceDto> list = new ArrayList<>();
        for(Device d : deviceRepository.findAll()) {
            list.add( DeviceDto.make(d) );
        }
        return list;
    }

    @Override
    public void remove(Long deviceId) {
        Device d = deviceRepository.findOne(deviceId);
        if(d == null) {
            throw new NotFoundException("device");
        }
        deviceRepository.delete(d);
    }
}
