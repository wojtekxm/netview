package zesp03.webapp.data.row;

import zesp03.common.entity.Controller;

public class ControllerRow {
    private long id;
    private String name;
    private String ipv4;
    private String description;
    private long building;

    public ControllerRow() {
    }

    /**
     * Controller entity should be in managed state.
     */
    public ControllerRow(Controller c) {
        this.id = c.getId();
        this.name = c.getName();
        this.ipv4 = c.getIpv4();
        this.description = c.getDescription();
        this.building = c.getBuilding().getId();
    }
    public ControllerRow( long id, String name, String ipv4, String description,long building ){

        this.id = id;
        this.name = name;
        this.ipv4 = ipv4;
        this.description = description;
        this.building = building;
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

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getBuilding(){return building; }

    public void setBuilding(long building) {this.building = building;}

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        ControllerRow other = (ControllerRow)obj;
        if( !(Long.valueOf( id ).equals( Long.valueOf( other.getId() ) )) )
            return false;

        return true;
    }
    public int hashCode(){
        return (int)id;
    }
}