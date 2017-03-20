package zesp03.webapp.dto;

import zesp03.common.entity.Device;

public class DeviceDto {
    private long id;
    private String name;
    private boolean known;
    private String description;
    private long controllerId;

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

    public void wrap(Device d) {
        this.id = d.getId();
        this.name = d.getName();
        this.known = d.isKnown();
        this.description = d.getDescription();
        this.controllerId = d.getController().getId();
    }

    public static DeviceDto make(Device d) {
        DeviceDto dto = new DeviceDto();
        dto.wrap(d);
        return dto;
    }
}
