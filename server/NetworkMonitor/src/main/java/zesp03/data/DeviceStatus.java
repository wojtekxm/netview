package zesp03.data;

import zesp03.entity.Controller;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;

public class DeviceStatus {
    private ControllerData controller;
    private DeviceData device;
    private DeviceSurveyData deviceSurvey;

    public DeviceStatus(ControllerData c, DeviceData d, DeviceSurveyData s) {
        this.controller = c;
        this.device = d;
        this.deviceSurvey = s;
    }

    public DeviceStatus(Controller c, Device d, DeviceSurvey s) {
        this(new ControllerData(c),
                new DeviceData(d),
                new DeviceSurveyData(s));
    }

    public ControllerData getController() {
        return controller;
    }

    public void setController(ControllerData controller) {
        this.controller = controller;
    }

    public DeviceData getDevice() {
        return device;
    }

    public void setDevice(DeviceData device) {
        this.device = device;
    }

    public DeviceSurveyData getDeviceSurvey() {
        return deviceSurvey;
    }

    public void setDeviceSurvey(DeviceSurveyData deviceSurvey) {
        this.deviceSurvey = deviceSurvey;
    }
}
