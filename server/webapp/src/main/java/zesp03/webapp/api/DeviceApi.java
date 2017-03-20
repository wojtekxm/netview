package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.core.Database;
import zesp03.common.entity.Device;
import zesp03.common.exception.NotFoundException;
import zesp03.common.service.SurveyService;
import zesp03.webapp.dto.BaseResultDto;
import zesp03.webapp.dto.DeviceStateDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DeviceApi {
    private final SurveyService surveyService;

    @Autowired
    public DeviceApi(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/api/all-devices")
    public List<DeviceStateDto> getAll() {
        return surveyService.checkAll()
                .stream()
                .map(DeviceStateDto::make)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/device")
    public DeviceStateDto getOne(
            @RequestParam("id") long device) {
        return DeviceStateDto.make( surveyService.checkOne(device) );
    }

    @PostMapping(value = "/api/remove-device", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto remove(
            @RequestParam("id") long id) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Device d = em.find(Device.class, id);
            if(d == null)
                throw new NotFoundException("device");
            em.remove(d);
            tran.commit();
            return new BaseResultDto(true);
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }
}
