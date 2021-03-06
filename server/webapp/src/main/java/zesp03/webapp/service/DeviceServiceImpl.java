/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.entity.Building;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.exception.NotFoundException;
import zesp03.common.repository.BuildingRepository;
import zesp03.common.repository.ControllerRepository;
import zesp03.common.repository.DeviceRepository;
import zesp03.common.repository.DeviceSurveyRepository;
import zesp03.common.service.SurveyReadingService;
import zesp03.webapp.dto.DeviceDetailsDto;
import zesp03.webapp.dto.DeviceDto;
import zesp03.webapp.dto.DeviceNowDto;
import zesp03.webapp.dto.input.LinkBuildingManyDevicesDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private BuildingRepository buildingRepository;

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
    public List<DeviceNowDto> checkAll() {
        return surveyReadingService.checkAllFetch()
                .values()
                .stream()
                .map(DeviceNowDto::make)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDetailsDto> checkDetailsAll() {
        //TODO sprawdź wydajność SQL
        return surveyReadingService.checkAllFetch()
                .values()
                .stream()
                .map(DeviceDetailsDto::make)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDetailsDto> checkDetailsAllNotInBuilding(Long buildingId) {
        return surveyReadingService.checkAllNotInBuildingFetch(buildingId)
                .values()
                .stream()
                .map(DeviceDetailsDto::make)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDetailsDto> checkDetailsByController(Long controllerId) {
        return surveyReadingService.checkForControllerFetch(controllerId)
                .values()
                .stream()
                .map(DeviceDetailsDto::make)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDetailsDto> checkDetailsByBuilding(Long buildingId) {
        return surveyReadingService.checkForBuildingFetch(buildingId)
                .values()
                .stream()
                .map(DeviceDetailsDto::make)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceNowDto checkOne(Long deviceId) {
        Optional<CurrentDeviceState> opt = surveyReadingService.checkOneFetch(deviceId);
        if( ! opt.isPresent() ) {
            throw new NotFoundException("device");
        }
        return DeviceNowDto.make( opt.get() );
    }

    @Override
    public DeviceDetailsDto checkDetailsOne(Long deviceId) {
        Optional<CurrentDeviceState> opt = surveyReadingService.checkOneFetch(deviceId);
        if( ! opt.isPresent() ) {
            throw new NotFoundException("device");
        }
        return DeviceDetailsDto.make( opt.get() );
    }

    @Override
    public void remove(Long deviceId) {
        Device d = findOneNotDeletedOrThrow(deviceId);
        d.setDeleted(d.getId());
        em.createQuery("UPDATE DeviceFrequency df SET df.deleted = df.id WHERE df.device = :d")
                .setParameter("d", d)
                .executeUpdate();
    }

    @Override
    public void linkController(Long deviceId, Long controllerId) {
        Optional<Controller> ocon = controllerRepository.findOneNotDeleted(controllerId);
        if(! ocon.isPresent() ) {
            throw new NotFoundException("controller");
        }
        findOneNotDeletedOrThrow(deviceId).setController(ocon.get());
        //TODO merge?
    }

    @Override
    public void linkBuilding(Long deviceId, Long buildingId) {
        Building bui = buildingRepository.findOne(buildingId);
        if(bui == null) {
            throw new NotFoundException("building");
        }
        findOneNotDeletedOrThrow(deviceId).setBuilding(bui);
    }

    @Override
    public void linkBuilding(LinkBuildingManyDevicesDto dto) {
        Building bui = buildingRepository.findOne(dto.getBuildingId());
        if(bui == null) {
            throw new NotFoundException("building");
        }
        for(Long deviceId : dto.getDeviceIds()) {
            findOneNotDeletedOrThrow(deviceId).setBuilding(bui);
        }
    }

    @Override
    public void unlinkController(Long deviceId) {
        findOneNotDeletedOrThrow(deviceId).setController(null);
    }

    @Override
    public void unlinkBuilding(Long deviceId) {
        findOneNotDeletedOrThrow(deviceId).setBuilding(null);
    }
}
