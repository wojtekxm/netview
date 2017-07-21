/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.webapp.dto.SearchDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.service.SearchService;

@RestController
public class SearchApi {
    @Autowired
    private SearchService searchService;

    @GetMapping("/api/search")
    public ContentDto<SearchDto> search(
            @RequestParam("q") String query) {
        return ContentDto.make( () -> searchService.search(query) );
    }
}
