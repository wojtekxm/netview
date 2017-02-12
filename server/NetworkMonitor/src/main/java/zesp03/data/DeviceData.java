package zesp03.data;

import zesp03.data.row.DeviceRow;
import zesp03.data.row.DeviceSurveyRow;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;

public class DeviceData {
    private long id;
    private String name;
    private boolean known;
    private String description;
    private long controllerId;
    private int lastSurveyTimestamp;
    private boolean enabled;
    private int clientsSum;

    public DeviceData() {
    }

    public DeviceData(DeviceRow device, DeviceSurveyRow survey) {
        this.id = device.getId();
        this.name = device.getName();
        this.known = device.isKnown();
        this.description = device.getDescription();
        this.controllerId = device.getControllerId();
        this.lastSurveyTimestamp = survey.getTimestamp();
        this.enabled = survey.isEnabled();
        this.clientsSum = survey.getClientsSum();
    }

    /**
     * Device and DeviceSurvey should be in managed state.
     */
    public DeviceData(Device device, DeviceSurvey survey) {
        this.id = device.getId();
        this.name = device.getName();
        this.known = device.isKnown();
        this.description = device.getDescription();
        this.controllerId = device.getController().getId();
        this.lastSurveyTimestamp = survey.getTimestamp();
        this.enabled = survey.isEnabled();
        this.clientsSum = survey.getClientsSum();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isKnown() {
        return known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getControllerId() {
        return controllerId;
    }

    public void setControllerId(long controllerId) {
        this.controllerId = controllerId;
    }

    public int getLastSurveyTimestamp() {
        return lastSurveyTimestamp;
    }

    public void setLastSurveyTimestamp(int lastSurveyTimestamp) {
        this.lastSurveyTimestamp = lastSurveyTimestamp;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getClientsSum() {
        return clientsSum;
    }

    public void setClientsSum(int clientsSum) {
        this.clientsSum = clientsSum;
    }
}
