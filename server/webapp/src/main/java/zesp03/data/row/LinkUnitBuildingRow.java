package zesp03.data.row;

import zesp03.entity.Building;
import zesp03.entity.LinkUnitBuilding;
import zesp03.entity.Unit;

/**
 * Created by Berent on 2017-03-06.
 */
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
