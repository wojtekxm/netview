package zesp03.webapp.dto;

import java.util.List;

public class BuildingUnitsControllersDto {
    private BuildingDto building;
    private List<UnitDto> units;
    private List<ControllerDto> controllers;

    public BuildingDto getBuilding() {
        return building;
    }

    public void setBuilding(BuildingDto building) {
        this.building = building;
    }

    public List<UnitDto> getUnits() {
        return units;
    }

    public void setUnits(List<UnitDto> units) {
        this.units = units;
    }

    public List<ControllerDto> getControllers() {
        return controllers;
    }

    public void setControllers(List<ControllerDto> controllers) {
        this.controllers = controllers;
    }
}
