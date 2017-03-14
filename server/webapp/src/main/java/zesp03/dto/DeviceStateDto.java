package zesp03.dto;

import zesp03.data.DeviceStateData;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;

public class DeviceStateDto {
    private long id;
    private String name;
    private boolean known;
    private String description;
    private long controllerId;
    private int lastSurveyTimestamp; // -1 oznacza brak badań
    private boolean enabled;
    private int clientsSum;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isKnown() {
        return known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getControllerId() {
        return controllerId;
    }

    public void setControllerId(long controllerId) {
        this.controllerId = controllerId;
    }

    public int getLastSurveyTimestamp() {
        return lastSurveyTimestamp;
    }

    public void setLastSurveyTimestamp(int lastSurveyTimestamp) {
        this.lastSurveyTimestamp = lastSurveyTimestamp;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getClientsSum() {
        return clientsSum;
    }

    public void setClientsSum(int clientsSum) {
        this.clientsSum = clientsSum;
    }

    public void wrap(DeviceStateData dsd) {
        Device dev = dsd.getDevice();
        DeviceSurvey sur = dsd.getSurvey();
        this.id = dev.getId();
        this.name = dev.getName();
        this.known = dev.isKnown();
        this.description = dev.getDescription();
        this.controllerId = dsd.getController().getId();
        if(sur != null) {
            this.lastSurveyTimestamp = sur.getTimestamp();
            this.enabled = sur.isEnabled();
            this.clientsSum = sur.getClientsSum();
        }
        else {
            this.lastSurveyTimestamp = -1;
            this.enabled = false;
            this.clientsSum = 0;
        }
    }
}
