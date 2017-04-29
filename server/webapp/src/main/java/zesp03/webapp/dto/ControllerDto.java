package zesp03.webapp.dto;

import zesp03.common.entity.Controller;

public class ControllerDto {
    private long id;
    private String name;
    private String ipv4;
    private String description;
    private String communityString;
    private Long buildingId;
    private boolean fake;

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

    public String getCommunityString() {
        return communityString;
    }

    public void setCommunityString(String communityString) {
        this.communityString = communityString;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public boolean isFake() {
        return fake;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }

    public void wrap(Controller c) {
        this.id = c.getId();
        this.name = c.getName();
        this.ipv4 = c.getIpv4();
        this.description = c.getDescription();
        this.communityString = c.getCommunity();
        if(c.getBuilding() != null) {
            this.buildingId = c.getBuilding().getId();
        }
        else {
            this.buildingId = null;
        }
        this.fake = c.isFake();
    }

    public static ControllerDto make(Controller c) {
        ControllerDto dto = new ControllerDto();
        dto.wrap(c);
        return dto;
    }
}