package zesp03.webapp.dto.input;

import java.util.List;

public class LinkBuildingManyDevicesDto {
    private List<Long> deviceIds;
    private Long buildingId;

    public List<Long> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<Long> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }
}
