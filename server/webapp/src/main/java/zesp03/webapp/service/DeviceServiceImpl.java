package zesp03.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.exception.NotFoundException;
import zesp03.common.repository.ControllerRepository;
import zesp03.common.repository.DeviceRepository;
import zesp03.common.repository.DeviceSurveyRepository;
import zesp03.common.service.SurveyReadingService;
import zesp03.webapp.dto.CurrentDeviceStateDto;
import zesp03.webapp.dto.DeviceDetailsDto;
import zesp03.webapp.dto.DeviceDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SurveyReadingService surveyReadingService;

    @Autowired
    private ControllerRepository controllerRepository;

    @Autowired
    private DeviceSurveyRepository deviceSurveyRepository;

    @Override
    public Device findOneNotDeletedOrThrow(Long deviceId) {
        Optional<Device> opt = deviceRepository.findOneNotDeleted(deviceId);
        if(!opt.isPresent()) {
            throw new NotFoundException("device");
        }
        return opt.get();
    }

    @Override
    public DeviceDto getOne(Long deviceId) {
        return DeviceDto.make( findOneNotDeletedOrThrow(deviceId) );
    }

    @Override
    public List<DeviceDto> getAll() {
        List<DeviceDto> list = new ArrayList<>();
        for(Device d : deviceRepository.findAllNotDeleted()) {
            list.add( DeviceDto.make(d) );
        }
        return list;
    }

    @Override
    public CurrentDeviceStateDto checkOne(Long deviceId) {
        Optional<CurrentDeviceState> opt = surveyReadingService.checkOne(deviceId);
        if( ! opt.isPresent() ) {
            throw new NotFoundException("device");
        }
        return CurrentDeviceStateDto.make( opt.get() );
    }

    @Override
    public List<CurrentDeviceStateDto> checkAll() {
        return surveyReadingService.checkAll()
                .values()
                .stream()
                .map(CurrentDeviceStateDto::make)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDetailsDto> checkDetailsAll() {
        //TODO sprawdź wydajność SQL
        Map<Long, Controller> map = new HashMap<>();
        for(Controller c : controllerRepository.findAllNotDeleted()) {
            map.put(c.getId(), c);
        }
        return surveyReadingService.checkAll()
                .values()
                .stream()
                .map( cds -> {
                    Controller c = map.get( cds.getDevice().getController().getId() );
                    return DeviceDetailsDto.make(cds, c);
                } )
                .collect(Collectors.toList());
    }

    @Override
    public Long countSurveys(Long deviceId) {
        Optional<Device> opt = deviceRepository.findOneNotDeleted(deviceId);
        if(!opt.isPresent()) {
            throw new NotFoundException("device");
        }
        Set<Long> ids = opt.get()
                .getFrequencyList()
                .stream()
                .map(DeviceFrequency::getId)
                .collect(Collectors.toSet());
        if(ids.isEmpty()) {
            return 0L;
        }
        return deviceSurveyRepository.countForDeviceFrequenciesNotDeleted(ids);
    }

    @Override
    public Long countSurveysBefore(Long deviceId, int before) {
        Optional<Device> opt = deviceRepository.findOneNotDeleted(deviceId);
        if(!opt.isPresent()) {
            throw new NotFoundException("device");
        }
        Set<Long> ids = opt.get()
                .getFrequencyList()
                .stream()
                .map(DeviceFrequency::getId)
                .collect(Collectors.toSet());
        if(ids.isEmpty()) {
            return 0L;
        }
        return deviceSurveyRepository.countBeforeForDeviceFrequenciesNotDeleted(ids, before);
    }

    @Override
    public void remove(Long deviceId) {
        Device d = findOneNotDeletedOrThrow(deviceId);
        d.setDeleted(true);
        em.createQuery("UPDATE DeviceFrequency df SET df.deleted = true WHERE df.device = :d")
                .setParameter("d", d)
                .executeUpdate();
    }
}
