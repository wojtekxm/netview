package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import zesp03.webapp.dto.DeviceDto;
import zesp03.webapp.service.DeviceService;

@Controller
public class DevicePage {
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/device/{deviceId}")
    public String getDevice(
            @PathVariable("deviceId") long deviceId,
            ModelMap model) {
        DeviceDto device = deviceService.getOne(deviceId);
        model.put("device", device);
        return "device";
    }

    @PostMapping("/device/remove/{deviceId}")
    public String postRemove(
            @PathVariable("deviceId") long deviceId) {
        deviceService.remove(deviceId);
        return "redirect:/all-devices";
    }
}
