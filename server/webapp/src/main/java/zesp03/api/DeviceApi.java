package zesp03.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.Database;
import zesp03.data.DeviceNow;
import zesp03.dto.BaseResultDto;
import zesp03.dto.DeviceStateDto;
import zesp03.entity.Device;
import zesp03.exception.NotFoundException;
import zesp03.service.SurveyService;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DeviceApi {
    @GetMapping("/api/all-devices")
    public List<DeviceStateDto> getAll() {
        return new SurveyService()
                .checkAll()
                .stream()
                .map( dn -> {
                    DeviceStateDto dto = new DeviceStateDto();
                    dto.wrap(dn);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/api/device")
    public DeviceStateDto getOne(
            @RequestParam("id") long device) {
        DeviceNow dn = new SurveyService().checkOne(device);
        DeviceStateDto dto = new DeviceStateDto();
        dto.wrap(dn);
        return dto;
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
