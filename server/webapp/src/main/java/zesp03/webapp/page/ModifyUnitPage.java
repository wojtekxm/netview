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
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.service.UnitService;


@Controller
public class ModifyUnitPage {
    @Autowired
    private UnitService unitService;

    @GetMapping("/modify-unit")
    public String get(
            @RequestParam("id") long id,
            ModelMap model) {
        UnitDto dto = unitService.getOne(id);
        model.put("unit", dto);
        return "modify-unit";
    }
}
