package zesp03.webapp.dto;

import zesp03.common.entity.DeviceSurvey;

@Deprecated
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

    public void wrap(DeviceNow d) {
        this.id = d.getId();
        this.name = d.getName();
        this.known = d.isKnown();
        this.description = d.getDescription();
        this.controllerId = d.getControllerId();
        DeviceSurvey s = d.getSurvey();
        if(s != null) {
            this.lastSurveyTimestamp = s.getTimestamp();
            this.enabled = s.isEnabled();
            this.clientsSum = s.getClientsSum();
        }
        else {
            this.lastSurveyTimestamp = -1;
            this.enabled = false;
            this.clientsSum = 0;
        }
    }

    public static DeviceStateDto make(DeviceNow d) {
        DeviceStateDto dto = new DeviceStateDto();
        dto.wrap(d);
        return dto;
    }
}
