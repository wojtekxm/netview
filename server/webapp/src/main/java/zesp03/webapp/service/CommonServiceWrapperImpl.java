package zesp03.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.entity.Controller;
import zesp03.common.exception.NotFoundException;
import zesp03.common.repository.ControllerRepository;
import zesp03.common.service.CurrentSurveyService;
import zesp03.webapp.dto.CurrentDeviceStateDto;
import zesp03.webapp.dto.DeviceDetailsDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommonServiceWrapperImpl implements CommonServiceWrapper {
    @Autowired
    private CurrentSurveyService currentSurveyService;

    @Autowired
    private ControllerRepository controllerRepository;

    @Override
    public CurrentDeviceStateDto checkOne(Long deviceId) {
        Optional<CurrentDeviceState> opt = currentSurveyService.checkOne(deviceId);
        if( ! opt.isPresent() ) {
            throw new NotFoundException("device");
        }
        return CurrentDeviceStateDto.make( opt.get() );
    }

    @Override
    public List<CurrentDeviceStateDto> checkAll() {
        return currentSurveyService.checkAll()
                .values()
                .stream()
                .map(CurrentDeviceStateDto::make)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDetailsDto> checkDetailsAll() {
        //TODO sprawdź wydajność SQL
        Map<Long, Controller> map = new HashMap<>();
        for(Controller c : controllerRepository.findAll()) {
            map.put(c.getId(), c);
        }
        return currentSurveyService.checkAll()
                .values()
                .stream()
                .map( cds -> {
                    Controller c = map.get( cds.getDevice().getController().getId() );
                    return DeviceDetailsDto.make(cds, c);
                } )
                .collect(Collectors.toList());
    }
}
