/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.service.BuildingService;
import zesp03.webapp.service.ControllerService;

import java.util.List;

@Controller
public class ModifyControllerPage {
    @Autowired
    private ControllerService controllerService;

    @Autowired
    private BuildingService buildingService;

    @GetMapping("/modify-controller")
    public String get(
            @RequestParam("id") Long id,
            ModelMap model) {
        ControllerDto dto = controllerService.modifyController(id);
        List<BuildingDto> list = buildingService.getAllBuildings();
        model.put("controller", dto);
        model.put("list", list);
        return "modify-controller";
    }
}
