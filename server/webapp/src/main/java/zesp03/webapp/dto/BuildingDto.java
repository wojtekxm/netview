package zesp03.webapp.dto;

import zesp03.common.entity.Building;

public class BuildingDto {
    private long id;
    private String code;
    private String name;
    private double latitude;
    private double longitude;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void wrap(Building b) {
        this.id = b.getId();
        this.code = b.getCode();
        this.name = b.getName();
        this.latitude = b.getLatitude().doubleValue();
        this.longitude = b.getLongitude().doubleValue();
    }
}
