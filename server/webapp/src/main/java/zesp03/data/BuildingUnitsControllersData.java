package zesp03.data;

import zesp03.data.row.BuildingRow;
import zesp03.data.row.ControllerRow;
import zesp03.data.row.UnitRow;
import zesp03.entity.Building;
import zesp03.entity.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Berent on 2017-03-14.
 */
public class BuildingUnitsControllersData {

    private BuildingRow building;
    private List<UnitRow> units = new ArrayList<>();
    private List<ControllerRow> controllers = new ArrayList<>();

    public BuildingUnitsControllersData(BuildingRow building, List<UnitRow> units, List<ControllerRow> controllers) {

        this.building= building;
        this.units.addAll( units );
        this.controllers.addAll(controllers);
    }

    public BuildingRow getBuilding() {

        return building;
    }
    public List < UnitRow > getUnits() {

        return units;
    }
    public List <ControllerRow> getControllers() {
        return controllers;
    }

}