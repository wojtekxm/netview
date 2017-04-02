package zesp03.webapp.dto;

import zesp03.common.entity.DeviceSurvey;

public class ShortSurveyDto {
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

    public static ShortSurveyDto make(DeviceSurvey s) {
        ShortSurveyDto dto = new ShortSurveyDto();
        dto.wrap(s);
        return dto;
    }
}
