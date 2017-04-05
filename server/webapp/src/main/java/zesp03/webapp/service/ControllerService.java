package zesp03.webapp.service;

import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.dto.input.CreateControllerDto;

import java.util.List;

public interface ControllerService {
    List<ControllerDto> getAll();
    ControllerDto getOne(Long controllerId);
    void remove(Long controllerId);
    void create(CreateControllerDto dto);
}
