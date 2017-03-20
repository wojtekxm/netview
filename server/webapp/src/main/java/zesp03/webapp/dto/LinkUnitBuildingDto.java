package zesp03.webapp.dto;

import zesp03.common.entity.Building;
import zesp03.common.entity.LinkUnitBuilding;
import zesp03.common.entity.Unit;

public class LinkUnitBuildingDto {
    private long id;
    private Building building;
    private Unit unit;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void wrap(LinkUnitBuilding lub){
        this.id = lub.getId();
        this.building = lub.getBuilding();
        this.unit = lub.getUnit();
    }

    public static LinkUnitBuildingDto make(LinkUnitBuilding lub) {
        LinkUnitBuildingDto dto = new LinkUnitBuildingDto();
        dto.wrap(lub);
        return dto;
    }
}
