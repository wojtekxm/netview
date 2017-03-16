package zesp03.data.row;

import zesp03.entity.Building;

import java.math.BigDecimal;

/**
 * Created by Berent on 2017-03-06.
 */


public class BuildingRow {
    private long id;
    private String code;
    private String name;
    private Number latitude;
    private Number longitude;

    public BuildingRow(){}

    public BuildingRow(Building b) {
        this.id = b.getId();
        this.code = b.getCode();
        this.name = b.getName();
        this.latitude = b.getLatitude();
        this.longitude = b.getLongitude();

    }
    public BuildingRow( long id, String code, String name, BigDecimal latitude, BigDecimal longitude ){

        this.id = id;
        this.code = code;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName(){return name;}

    public void setName(String name) {this.name = name; }

    public Number getLatitude() {return latitude; }

    public void setLatitude(BigDecimal latitude) {this.latitude = latitude; }

    public Number getLongitude() { return longitude; }

    public void setLongitude(BigDecimal longitude) {this.longitude = longitude;}

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        BuildingRow other = (BuildingRow)obj;
        if( !(Long.valueOf( id ).equals( Long.valueOf( other.getId() ) )) )
            return false;

        return true;
    }
    public int hashCode(){
        return (int)id;
    }
}
