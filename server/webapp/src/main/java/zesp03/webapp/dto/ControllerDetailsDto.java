/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.dto;

import zesp03.common.entity.Controller;

public class ControllerDetailsDto {
    private long id;
    private String name;
    private String ipv4;
    private String description;
    private String communityString;
    private boolean fake;
    private BuildingDto building;
    private int numberOfDevices;

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

    public String getCommunityString() {
        return communityString;
    }

    public void setCommunityString(String communityString) {
        this.communityString = communityString;
    }

    public boolean isFake() {
        return fake;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }

    public int getNumberOfDevices() {
        return numberOfDevices;
    }

    public void setNumberOfDevices(int numberOfDevices) {
        this.numberOfDevices = numberOfDevices;
    }

    public BuildingDto getBuilding() {
        return building;
    }

    public void setBuilding(BuildingDto building) {
        this.building = building;
    }

    public void wrap(Controller c) {
        wrap(c, c.getDeviceList().size());
    }

    public void wrap(Controller c, int numberOfDevices) {
        this.id = c.getId();
        this.name = c.getName();
        this.ipv4 = c.getIpv4();
        this.description = c.getDescription();
        this.communityString = c.getCommunity();
        this.fake = c.isFake();
        this.numberOfDevices = numberOfDevices;
        if(c.getBuilding() != null) {
            this.building = BuildingDto.make(c.getBuilding());
        }
        else {
            this.building = null;
        }
    }

    public static ControllerDetailsDto make(Controller c, int numberOfDevices) {
        ControllerDetailsDto dto = new ControllerDetailsDto();
        dto.wrap(c, numberOfDevices);
        return dto;
    }
}
