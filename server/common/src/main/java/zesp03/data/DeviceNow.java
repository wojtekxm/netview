package zesp03.data;

public class DeviceNow {
    private Long id;
    private String name;
    private Boolean known;
    private String description;
    private Long controllerId;
    private Long surveyId;
    private Integer surveyTime;
    private Boolean surveyEnabled;
    private Integer surveyClients;
    private Long surveyCumulative;

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

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Integer getSurveyTime() {
        return surveyTime;
    }

    public void setSurveyTime(Integer surveyTime) {
        this.surveyTime = surveyTime;
    }

    public Boolean isSurveyEnabled() {
        return surveyEnabled;
    }

    public void setSurveyEnabled(Boolean surveyEnabled) {
        this.surveyEnabled = surveyEnabled;
    }

    public Integer getSurveyClients() {
        return surveyClients;
    }

    public void setSurveyClients(Integer surveyClients) {
        this.surveyClients = surveyClients;
    }

    public Long getSurveyCumulative() {
        return surveyCumulative;
    }

    public void setSurveyCumulative(Long surveyCumulative) {
        this.surveyCumulative = surveyCumulative;
    }
}
