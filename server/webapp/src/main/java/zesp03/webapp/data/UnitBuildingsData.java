package zesp03.webapp.data;

import zesp03.webapp.data.row.BuildingRow;
import zesp03.webapp.data.row.UnitRow;

import java.util.ArrayList;
import java.util.List;


    public class UnitBuildingsData {

        private UnitRow unit;
        private List<BuildingRow> buildings = new ArrayList<>();

        public UnitBuildingsData(UnitRow unit, List<BuildingRow> buildings) {

            this.unit= unit;
            this.buildings.addAll( buildings);
        }

        public UnitRow getUnit() {

            return unit;
        }
        public List< BuildingRow > getBuildings() {

            return buildings;
        }
    }