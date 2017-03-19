package zesp03.webapp.data;

import zesp03.webapp.data.row.BuildingRow;
import zesp03.webapp.data.row.ControllerRow;
import zesp03.webapp.data.row.UnitRow;

import java.util.ArrayList;
import java.util.List;

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