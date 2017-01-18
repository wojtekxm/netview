package zesp03.data;

import zesp03.entity.DeviceSurvey;

public class SurveyData {
    private Long id;
    private Integer timestamp;
    private Boolean isEnabled;
    private Integer clientsSum;

    public SurveyData(DeviceSurvey s) {
        this.id = s.getId();
        this.timestamp = s.getTimestamp();
        this.isEnabled = s.isEnabled();
        this.clientsSum = s.getClientsSum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Integer getClientsSum() {
        return clientsSum;
    }

    public void setClientsSum(Integer clientsSum) {
        this.clientsSum = clientsSum;
    }
}
