package zesp03.dto;

public class OriginalSurveyDto {
    private int timestamp;
    private int clientsSum;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getClientsSum() {
        return clientsSum;
    }

    public void setClientsSum(int clientsSum) {
        this.clientsSum = clientsSum;
    }
}
