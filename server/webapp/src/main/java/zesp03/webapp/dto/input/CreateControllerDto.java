/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.dto.input;

public class CreateControllerDto {

    private String name;
    private String description;
    private String ipv4;
    private Long buildingId;
    private String communityString;
    private Boolean fake;

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

    public Boolean isfake() {
        return fake;
    }

    public void setfake(Boolean fake) {
        this.fake = fake;
    }
}
