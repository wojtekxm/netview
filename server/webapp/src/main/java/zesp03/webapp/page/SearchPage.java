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
import zesp03.webapp.dto.SearchDto;
import zesp03.webapp.service.SearchService;

@Controller
public class SearchPage {
    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public String get(
            @RequestParam("query") String query,
            ModelMap model) {
        SearchDto dto = searchService.search(query);
        model.put("search", dto);
        return "search";
    }
}
