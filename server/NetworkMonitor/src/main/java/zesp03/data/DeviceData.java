package zesp03.data;

import zesp03.entity.Device;

public class DeviceData {
    private long id;
    private String name;
    private boolean isKnown;
    private String description;

    public DeviceData(Device d) {
        this.id = d.getId();
        this.name = d.getName();
        this.isKnown = d.isKnown();
        this.description = d.getDescription();
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

    public boolean getKnown() {
        return isKnown;
    }

    public void setKnown(boolean known) {
        isKnown = known;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
