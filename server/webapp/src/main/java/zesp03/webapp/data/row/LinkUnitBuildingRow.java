package zesp03.webapp.data.row;

import zesp03.common.entity.Building;
import zesp03.common.entity.LinkUnitBuilding;
import zesp03.common.entity.Unit;

public class LinkUnitBuildingRow {
    private long id;
    private Building building;
    private Unit unit;

    public LinkUnitBuildingRow(){

    }

    public LinkUnitBuildingRow(LinkUnitBuilding lub){
        this.id = lub.getId();
        this.building = lub.getBuilding();
        this.unit = lub.getUnit();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Building getBuilding(){return building; }

    public void setBuilding(Building building) {this.building = building;}

    public Unit getUnit(){return unit; }

    public void setUnit(Unit unit) {this.unit = unit; }
}
