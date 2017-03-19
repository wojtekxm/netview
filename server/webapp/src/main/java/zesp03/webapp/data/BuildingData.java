package zesp03.webapp.data;

import zesp03.common.entity.Building;
import zesp03.common.entity.LinkUnitBuilding;
import zesp03.webapp.data.row.BuildingRow;
import zesp03.webapp.data.row.UnitRow;

public class BuildingData {
    private long id;
    private String code;
    private String name;
    private Number latitude;
    private Number longitude;
    private Long unitId;
    private String unitCode;
    private String unitDescription;

    public BuildingData(){

    }
public BuildingData(BuildingRow building, UnitRow unit) {
        this.id = building.getId();
        this.code = building.getCode();
        this.name = building.getName();
        this.longitude = building.getLongitude();
        this.latitude = building.getLatitude();
        this.unitId = unit.getId();
        this.unitCode = unit.getCode();
        this.unitDescription = unit.getDescription();

}
    public BuildingData(Building building, LinkUnitBuilding linkunitbuilding) {
        this.id = building.getId();
        this.code = building.getCode();
        this.name = building.getName();
        this.longitude = building.getLongitude();
        this.latitude = building.getLatitude();
        this.unitId = linkunitbuilding.getUnit().getId();
        this.unitCode = linkunitbuilding.getUnit().getCode();
        this.unitDescription = linkunitbuilding.getUnit().getDescription();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Number getLatitude() {
        return latitude;
    }

    public void setLatitude(Number latitude) {
        this.latitude = latitude;
    }

    public Number getLongitude() {
        return longitude;
    }

    public void setLongitude(Number longitude) { this.longitude = longitude;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

}
