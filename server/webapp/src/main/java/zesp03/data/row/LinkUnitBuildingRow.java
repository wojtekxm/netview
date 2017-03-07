package zesp03.data.row;

import zesp03.entity.Building;
import zesp03.entity.Link_unit_building;
import zesp03.entity.Unit;

/**
 * Created by Berent on 2017-03-06.
 */
public class LinkUnitBuildingRow {
    private Long id;
    private Building building;
    private Unit unit;

    public LinkUnitBuildingRow(){

    }

    public LinkUnitBuildingRow(Link_unit_building lub){
        this.id = lub.getId();
        this.building = lub.getBuilding();
        this.unit = lub.getUnit();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Building getBuilding(){return building; }

    public void setBuilding(Building building) {this.building = building;}

    public Unit getUnit(){return unit; }

    public void setUnit(Unit unit) {this.unit = unit; }
}
