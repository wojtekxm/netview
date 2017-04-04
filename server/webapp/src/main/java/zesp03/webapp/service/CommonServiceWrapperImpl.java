package zesp03.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.exception.NotFoundException;
import zesp03.common.service.CurrentSurveyService;
import zesp03.webapp.dto.CurrentDeviceStateDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommonServiceWrapperImpl implements CommonServiceWrapper {
    @Autowired
    private CurrentSurveyService currentSurveyService;

    @Override
    public CurrentDeviceStateDto checkOneDevice(Long deviceId) {
        Optional<CurrentDeviceState> opt = currentSurveyService.checkOne(deviceId);
        if( ! opt.isPresent() ) {
            throw new NotFoundException("device");
        }
        return CurrentDeviceStateDto.make( opt.get() );
    }

    @Override
    public List<CurrentDeviceStateDto> checkAllDevices() {
        return currentSurveyService.checkAll()
                .values()
                .stream()
                .map(CurrentDeviceStateDto::make)
                .collect(Collectors.toList());
    }
}
