package zesp03.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.entity.Building;
import zesp03.common.entity.Controller;
import zesp03.common.exception.NotFoundException;
import zesp03.common.exception.ValidationException;
import zesp03.common.repository.BuildingRepository;
import zesp03.common.repository.ControllerRepository;
import zesp03.common.util.IPv4;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.ControllerDetailsDto;
import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.dto.input.CreateControllerDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

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
    public List<ControllerDto> getAll() {
        List<ControllerDto> r = new ArrayList<>();
        for(Controller c : controllerRepository.findAll()) {
            r.add( ControllerDto.make(c) );
        }
        return r;
    }

    @Override
    public ControllerDto getOne(Long controllerId) {
        Controller c = controllerRepository.findOne(controllerId);
        if(c == null) {
            throw new NotFoundException("controller");
        }
        return ControllerDto.make(c);
    }

    @Override
    public List<ControllerDetailsDto> getDetailsAll() {
        List<ControllerDetailsDto> result = new ArrayList<>();
        for(Controller c : controllerRepository.findAll()) {
            result.add(ControllerDetailsDto.make(c));
        }
        return result;
    }

    @Override
    public ControllerDetailsDto getDetailsOne(Long controllerId) {
        Controller c = controllerRepository.findOne(controllerId);
        if(c == null) {
            throw new NotFoundException("controller");
        }
        return ControllerDetailsDto.make(c);
    }

    @Override
    public void remove(Long controllerId) {
        Controller c = controllerRepository.findOne(controllerId);
        if(c == null) {
            throw new NotFoundException("controller");
        }
        controllerRepository.delete(c);
    }

    @Override
    public void create(CreateControllerDto dto) {
        if(dto.getIpv4() == null) {
            throw new ValidationException("ipv4", "null");
        }
        if(dto.getName() == null) {
            throw new ValidationException("ipv4", "null");
        }
        if( ! IPv4.isValid( dto.getIpv4() ) ) {
            throw new ValidationException("ipv4", "invalid format");
        }

        //TODO co jeśli taki kontroler już istnieje?
        Controller c = new Controller();
        c.setName(dto.getName());
        c.setIpv4(dto.getIpv4());
        c.setDescription(dto.getDescription());
        c.setCommunityString(dto.getCommunityString());
        if(dto.getBuildingId() != null) {
            Building b = buildingRepository.findOne(dto.getBuildingId());
            c.setBuilding(b);
        }
        controllerRepository.save(c);
    }


    @Override
    public ControllerDto modifyController(Long controllerId) {
        Controller c = controllerRepository.findOne(controllerId);
        if(c == null)
            throw new NotFoundException("controller");
        return ControllerDto.make(c);
    }

    @Override
    public void acceptModifyController(ControllerDto dto) {

        if(dto.getIpv4() == null) {
            throw new ValidationException("ipv4", "null");
        }
        if(dto.getName() == null) {
            throw new ValidationException("ipv4", "null");
        }
        if( ! IPv4.isValid( dto.getIpv4() ) ) {
            throw new ValidationException("ipv4", "invalid format");
        }
        Controller c = controllerRepository.findOne(dto.getId());

        if(c == null) {
            if (em != null)
                em.close();
            throw new NotFoundException("acceptModifyController");
        }

        c.setName(dto.getName());
        c.setIpv4(dto.getIpv4());
        c.setDescription(dto.getDescription());
        c.setCommunityString(dto.getCommunityString());
        if(dto.getBuildingId() != null) {
            Building b = buildingRepository.findOne(dto.getBuildingId());
            c.setBuilding(b);
        }
        controllerRepository.save(c);

        }


    }

