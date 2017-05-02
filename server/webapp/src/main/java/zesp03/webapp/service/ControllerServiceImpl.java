package zesp03.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.entity.Building;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.exception.NotFoundException;
import zesp03.common.exception.ValidationException;
import zesp03.common.repository.BuildingRepository;
import zesp03.common.repository.ControllerRepository;
import zesp03.common.util.IPv4;
import zesp03.webapp.dto.ControllerDetailsDto;
import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.dto.input.CreateControllerDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ControllerServiceImpl implements ControllerService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ControllerRepository controllerRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Override
    public Controller findOneNotDeletedOrThrow(Long controllerId) {
        Optional<Controller> opt = controllerRepository.findOneNotDeleted(controllerId);
        if(!opt.isPresent()) {
            throw new NotFoundException("controller");
        }
        return opt.get();
    }

    @Override
    public List<ControllerDto> getAll() {
        List<ControllerDto> r = new ArrayList<>();
        for(Controller c : controllerRepository.findAllNotDeleted()) {
            r.add( ControllerDto.make(c) );
        }
        return r;
    }

    @Override
    public ControllerDto getOne(Long controllerId) {
        return ControllerDto.make( findOneNotDeletedOrThrow(controllerId) );
    }

    @Override
    public List<ControllerDetailsDto> getDetailsAll() {
        List<ControllerDetailsDto> result = new ArrayList<>();
        for(Controller c : controllerRepository.findAllNotDeleted()) {
            result.add(ControllerDetailsDto.make(c));
        }
        return result;
    }

    @Override
    public ControllerDetailsDto getDetailsOne(Long controllerId) {
        return ControllerDetailsDto.make( findOneNotDeletedOrThrow(controllerId) );
    }

    @Override
    public void remove(Long controllerId) {
        Controller c =  findOneNotDeletedOrThrow(controllerId);
        c.setDeleted(true);
        em.createQuery("UPDATE Device dev SET dev.deleted = true WHERE dev.controller = :c")
                .setParameter("c", c)
                .executeUpdate();
        List<Long> deviceIdsToDelete = em.createQuery("SELECT dev FROM Device dev " +
                        "WHERE dev.controller = :c",
                Device.class)
                .setParameter("c", c)
                .getResultList()
                .stream()
                .map(Device::getId)
                .collect(Collectors.toList());
        if(deviceIdsToDelete.isEmpty())return;
        em.createQuery("UPDATE DeviceFrequency df SET df.deleted = true WHERE df.device.id IN (:ids)")
                .setParameter("ids", deviceIdsToDelete)
                .executeUpdate();
    }

    @Override
    public void create(CreateControllerDto dto) {
        if(dto.getIpv4() == null) {
            throw new ValidationException("ipv4", "null");
        }
        if(dto.getName().isEmpty()) {
            throw new ValidationException("name", "null");
        }
        if( ! IPv4.isValid( dto.getIpv4() ) ) {
            throw new ValidationException("ipv4", "invalid format");
        }

        //TODO co jeśli taki kontroler już istnieje?
        Controller c = new Controller();
        c.setName(dto.getName());
        c.setIpv4(dto.getIpv4());
        c.setDescription(dto.getDescription());
        c.setCommunity(dto.getCommunityString());
        c.setDeleted(false);
        c.setFake(true);//???
        if(dto.getBuildingId() != null) {
            Building b = buildingRepository.findOne(dto.getBuildingId());
            c.setBuilding(b);
        }
        controllerRepository.save(c);
    }


    @Override
    public ControllerDto modifyController(Long controllerId) {
        return ControllerDto.make(findOneNotDeletedOrThrow(controllerId));
    }

    @Override
    public void acceptModifyController(ControllerDto dto) {

        if(dto.getIpv4() == null) {
            throw new ValidationException("ipv4", "null");
        }
        if( dto.getName() == null || dto.getName().isEmpty()) {
            throw new ValidationException("name", "null");
        }
        if( ! IPv4.isValid( dto.getIpv4() ) ) {
            throw new ValidationException("ipv4", "invalid format");
        }
        Controller c = findOneNotDeletedOrThrow(dto.getId());

        c.setName(dto.getName());
        c.setIpv4(dto.getIpv4());
        c.setDescription(dto.getDescription());
        c.setCommunity(dto.getCommunityString());
        if(dto.getBuildingId() != null) {
            Building b = buildingRepository.findOne(dto.getBuildingId());
            c.setBuilding(b);
        }
        controllerRepository.save(c);

        }


    }

