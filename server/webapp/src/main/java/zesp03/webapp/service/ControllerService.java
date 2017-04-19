package zesp03.webapp.service;

import zesp03.webapp.dto.ControllerDetailsDto;
import zesp03.common.entity.Building;
import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.dto.input.CreateControllerDto;

import java.util.List;

public interface ControllerService {
    List<ControllerDto> getAll();
    ControllerDto getOne(Long controllerId);
    List<ControllerDetailsDto> getDetailsAll();
    ControllerDetailsDto getDetailsOne(Long controllerId);
    void remove(Long controllerId);
    void create(CreateControllerDto dto);

}
