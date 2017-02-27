package zesp03.data.row;

import zesp03.entity.Controller;

public class ControllerRow {
    private long id;
    private String name;
    private String ipv4;
    private String description;

    public ControllerRow() {
    }

    /**
     * Controller entity should be in managed state.
     */
    public ControllerRow(Controller c) {
        this.id = c.getId();
        this.name = c.getName();
        this.ipv4 = c.getIpv4();
        this.description = c.getDescription();
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

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
