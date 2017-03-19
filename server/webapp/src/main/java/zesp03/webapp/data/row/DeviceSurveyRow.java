package zesp03.webapp.data.row;

import zesp03.common.entity.DeviceSurvey;

public class DeviceSurveyRow {
    private long id;
    private int timestamp;
    private boolean enabled;
    private int clientsSum;
    private long deviceId;

    public DeviceSurveyRow() {
    }

    /**
     * DeviceSurvey entity should be in managed state.
     */
    public DeviceSurveyRow(DeviceSurvey s) {
        this.id = s.getId();
        this.timestamp = s.getTimestamp();
        this.enabled = s.isEnabled();
        this.clientsSum = s.getClientsSum();
        this.deviceId = s.getDevice().getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
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

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }
}
