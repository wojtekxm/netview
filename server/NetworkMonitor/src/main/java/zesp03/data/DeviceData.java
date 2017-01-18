package zesp03.data;

import zesp03.entity.Device;

public class DeviceData {
    private Long id;
    private String name;
    private Boolean isKnown;
    private String description;

    public DeviceData(Device d) {
        this.id = d.getId();
        this.name = d.getName();
        this.isKnown = d.isKnown();
        this.description = d.getDescription();
    }

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

    public Boolean getKnown() {
        return isKnown;
    }

    public void setKnown(Boolean known) {
        isKnown = known;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
