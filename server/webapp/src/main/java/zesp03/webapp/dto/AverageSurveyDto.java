package zesp03.webapp.dto;

public class AverageSurveyDto {
    private long deviceId;
    private int timeStart; // timestamp w sekundach, przedział zamknięty
    private int timeEnd; // timestamp w sekundach, przedział otwarty
    private double avgClients;

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public int getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(int timeStart) {
        this.timeStart = timeStart;
    }

    public int getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(int timeEnd) {
        this.timeEnd = timeEnd;
    }

    public double getAvgClients() {
        return avgClients;
    }

    public void setAvgClients(double avgClients) {
        this.avgClients = avgClients;
    }
}
