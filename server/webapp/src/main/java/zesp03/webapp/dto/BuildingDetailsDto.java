package zesp03.webapp.dto;

import zesp03.common.entity.Building;

import java.util.List;
import java.util.stream.Collectors;

public class BuildingDetailsDto {
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

    public void wrap(Building b) {
        this.building = BuildingDto.make(b);
        this.units = b.getLubList()
                .stream()
                .map( lub -> UnitDto.make(lub.getUnit()) )
                .collect(Collectors.toList());
        this.controllers = b.getControllerList()
                .stream()
                .map(ControllerDto::make)
                .collect(Collectors.toList());
    }

    public static BuildingDetailsDto make(Building b) {
        BuildingDetailsDto dto = new BuildingDetailsDto();
        dto.wrap(b);
        return dto;
    }
}
