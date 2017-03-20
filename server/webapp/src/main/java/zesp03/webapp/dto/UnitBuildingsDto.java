package zesp03.webapp.dto;

import java.util.List;

public class UnitBuildingsDto {
    private UnitDto unit;
    private List<BuildingDto> buildings;

    public UnitDto getUnit() {
        return unit;
    }

    public void setUnit(UnitDto unit) {
        this.unit = unit;
    }

    public List<BuildingDto> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingDto> buildings) {
        this.buildings = buildings;
    }
}
