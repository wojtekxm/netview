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
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.service.BuildingService;

import java.util.List;

@Controller
public class AllBuildingsPage {
    @Autowired
    private BuildingService buildingService;

    @GetMapping("/all-buildings")
    public String get(ModelMap model) {
        List<BuildingDto> list = buildingService.getAllBuildings();
        model.put("list", list);
        return "all-buildings";
    }
}
