package zesp03.pojo;

public class CheckInfo {
    private final ControllerRow controller;
    private final DeviceRow device;
    private final SurveyRow survey;

    public CheckInfo(ControllerRow controller, DeviceRow device, SurveyRow survey) {
        this.controller = controller;
        this.device = device;
        this.survey = survey;
    }

    public ControllerRow controller() {
        return controller;
    }

    public DeviceRow device() {
        return device;
    }

    public SurveyRow survey() {
        return survey;
    }
}
