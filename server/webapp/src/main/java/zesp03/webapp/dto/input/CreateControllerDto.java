package zesp03.webapp.dto.input;

public class CreateControllerDto {
    private String name;
    private String description;
    private String ipv4;
    private Long buildingId;
    private String communityString;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getCommunityString() { return communityString; }

    public void setCommunityString(String communityString) {
        this.communityString = communityString;
    }
}
