package zesp03.data;

import zesp03.entity.Controller;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;

public class DeviceStatus {
    private Controller controller;
    private Device device;
    private DeviceSurvey survey;

    public DeviceStatus() {
    }

    public DeviceStatus(Controller c, Device d, DeviceSurvey s) {
        this.controller = c;
        this.device = d;
        this.survey = s;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public DeviceSurvey getSurvey() {
        return survey;
    }

    public void setSurvey(DeviceSurvey survey) {
        this.survey = survey;
    }
}
