package zesp03.data;

import zesp03.entity.DeviceSurvey;

public class SurveyData {
    private long id;
    private int timestamp;
    private boolean isEnabled;
    private int clientsSum;

    public SurveyData(DeviceSurvey s) {
        this.id = s.getId();
        this.timestamp = s.getTimestamp();
        this.isEnabled = s.isEnabled();
        this.clientsSum = s.getClientsSum();
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

    public boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public int getClientsSum() {
        return clientsSum;
    }

    public void setClientsSum(int clientsSum) {
        this.clientsSum = clientsSum;
    }
}
