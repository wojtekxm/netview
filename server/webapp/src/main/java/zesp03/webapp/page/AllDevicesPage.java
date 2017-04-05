package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import zesp03.webapp.dto.DeviceDto;
import zesp03.webapp.service.DeviceService;

import java.util.List;

@Controller
public class AllDevicesPage {
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/all-devices")
    public String get(ModelMap model) {
        List<DeviceDto> list = deviceService.getAll();
        model.put("list", list);
        return "all-devices";
    }
}
