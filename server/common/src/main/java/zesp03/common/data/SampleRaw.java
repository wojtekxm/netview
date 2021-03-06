/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.data;

import zesp03.common.entity.DeviceSurvey;

public class SampleRaw {
    private int timestamp;
    private int clients;
    private boolean enabled;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getClients() {
        return clients;
    }

    public void setClients(int clients) {
        this.clients = clients;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void wrap(DeviceSurvey s) {
        this.timestamp = s.getTimestamp();
        this.clients = s.getClientsSum();
        this.enabled = s.isEnabled();
    }

    public static SampleRaw make(DeviceSurvey s) {
        SampleRaw dto = new SampleRaw();
        dto.wrap(s);
        return dto;
    }
}
