package zesp03.webapp.dto;

import zesp03.common.entity.Building;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;

public class DeviceDto {
    private long id;
    private String name;
    private Long controllerId;
    private Long buildingId;

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

    public Long getControllerId() {
        return controllerId;
    }

    public void setControllerId(Long controllerId) {
        this.controllerId = controllerId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public void wrap(Device dev) {
        final Controller con = dev.getController();
        final Building b = dev.getBuilding();
        this.id = dev.getId();
        this.name = dev.getName();
        if(con != null) {
            this.controllerId = con.getId();
        }
        else {
            this.controllerId = null;
        }
        if(b != null) {
            this.buildingId = b.getId();
        }
        else {
            this.buildingId = null;
        }
    }

    public static DeviceDto make(Device d) {
        DeviceDto dto = new DeviceDto();
        dto.wrap(d);
        return dto;
    }
}
