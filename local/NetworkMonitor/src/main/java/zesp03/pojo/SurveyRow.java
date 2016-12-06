package zesp03.pojo;

public class SurveyRow {
    private int id;
    private boolean isEnabled;
    private int clientsSum;
    private int timestamp;
    private int deviceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getClientsSum() {
        return clientsSum;
    }

    public void setClientsSum(int clientsSum) {
        this.clientsSum = clientsSum;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}
