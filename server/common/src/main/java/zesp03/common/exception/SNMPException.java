/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.exception;

public class SNMPException extends BaseException {
    public SNMPException(String message) {
        super(message);
    }

    public SNMPException(String message, Throwable cause) {
        super(message, cause);
    }
}
