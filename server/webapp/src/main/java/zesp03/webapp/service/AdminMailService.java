/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.service;

public interface AdminMailService {
    boolean send(String targetMail, String subject, String htmlBody);
}
