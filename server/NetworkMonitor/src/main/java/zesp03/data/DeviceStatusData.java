package zesp03.data;

import zesp03.data.row.ControllerRow;
import zesp03.data.row.DeviceRow;
import zesp03.data.row.DeviceSurveyRow;
import zesp03.entity.Controller;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;

@Deprecated
public class DeviceStatusData {
    private ControllerRow controller;
    private DeviceRow device;
    private DeviceSurveyRow deviceSurvey;

    public DeviceStatusData(ControllerRow c, DeviceRow d, DeviceSurveyRow s) {
        this.controller = c;
        this.device = d;
        this.deviceSurvey = s;
    }

    public DeviceStatusData(Controller c, Device d, DeviceSurvey s) {
        this(new ControllerRow(c),
                new DeviceRow(d),
                new DeviceSurveyRow(s));
    }

    public ControllerRow getController() {
        return controller;
    }

    public void setController(ControllerRow controller) {
        this.controller = controller;
    }

    public DeviceRow getDevice() {
        return device;
    }

    public void setDevice(DeviceRow device) {
        this.device = device;
    }

    public DeviceSurveyRow getDeviceSurvey() {
        return deviceSurvey;
    }

    public void setDeviceSurvey(DeviceSurveyRow deviceSurvey) {
        this.deviceSurvey = deviceSurvey;
    }
}
