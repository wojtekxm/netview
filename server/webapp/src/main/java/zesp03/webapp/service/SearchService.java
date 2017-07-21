/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.service;

import zesp03.webapp.dto.SearchDto;

public interface SearchService {
    SearchDto search(String query);
}
