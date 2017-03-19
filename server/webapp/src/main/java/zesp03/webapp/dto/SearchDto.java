package zesp03.webapp.dto;

import java.util.List;

public class SearchDto {
    private List<DeviceDto> devices;
    private List<ControllerDto> controllers;
    private List<BuildingDto> buildings;
    private List<UnitDto> units;
    private List<UserDto> users;

    public List<DeviceDto> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceDto> devices) {
        this.devices = devices;
    }

    public List<ControllerDto> getControllers() {
        return controllers;
    }

    public void setControllers(List<ControllerDto> controllers) {
        this.controllers = controllers;
    }

    public List<BuildingDto> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingDto> buildings) {
        this.buildings = buildings;
    }

    public List<UnitDto> getUnits() {
        return units;
    }

    public void setUnits(List<UnitDto> units) {
        this.units = units;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
