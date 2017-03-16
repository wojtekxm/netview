package zesp03.data;

import zesp03.entity.DeviceSurvey;

public class DeviceNow {
    private Long id;
    private String name;
    private Boolean known;
    private String description;
    private Long controllerId;
    private DeviceSurvey survey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isKnown() {
        return known;
    }

    public void setKnown(Boolean known) {
        this.known = known;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getControllerId() {
        return controllerId;
    }

    public void setControllerId(Long controllerId) {
        this.controllerId = controllerId;
    }

    // może być null
    public DeviceSurvey getSurvey() {
        return survey;
    }

    public void setSurvey(DeviceSurvey survey) {
        this.survey = survey;
    }
}
