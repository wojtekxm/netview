package zesp03.data;

import zesp03.data.row.BuildingRow;
import zesp03.data.row.UnitRow;
import zesp03.entity.Building;
import zesp03.entity.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Berent on 2017-03-14.
 */
public class BuildingUnitsData {

    private BuildingRow building;
    private List<UnitRow> units = new ArrayList<>();

    public BuildingUnitsData(BuildingRow building, List<UnitRow> units ) {

        this.building= building;
        this.units.addAll( units );
    }

    public BuildingRow getBuilding() {

        return building;
    }
    public List< UnitRow > getUnits() {

        return units;
    }
}