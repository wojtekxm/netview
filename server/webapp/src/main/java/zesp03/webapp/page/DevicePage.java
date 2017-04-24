package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.dto.DeviceDto;
import zesp03.webapp.service.ControllerService;
import zesp03.webapp.service.DeviceService;

@Controller
public class DevicePage {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ControllerService controllerService;

    @GetMapping("/device/{deviceId}")
    public String getDevice(
            @PathVariable("deviceId") long deviceId,
            ModelMap model) {
        DeviceDto device = deviceService.getOne(deviceId);
        ControllerDto controller = controllerService.getOne(device.getControllerId());
        model.put("device", device);
        model.put("controller", controller);
        return "device";
    }
}
