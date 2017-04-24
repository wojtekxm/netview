package zesp03.webapp.service;

import zesp03.common.entity.Controller;
import zesp03.webapp.dto.ControllerDetailsDto;
import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.dto.input.CreateControllerDto;

import java.util.List;

public interface ControllerService {
    Controller findOneNotDeletedOrThrow(Long controllerId);
    List<ControllerDto> getAll();
    ControllerDto getOne(Long controllerId);
    List<ControllerDetailsDto> getDetailsAll();
    ControllerDetailsDto getDetailsOne(Long controllerId);
    void remove(Long controllerId);
    void create(CreateControllerDto dto);
    ControllerDto modifyController(Long controllerId);
    void acceptModifyController(ControllerDto dto);
}
