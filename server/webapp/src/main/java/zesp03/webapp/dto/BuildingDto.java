package zesp03.webapp.dto;

import zesp03.common.entity.Building;

public class BuildingDto {
    private long id;
    private String code;
    private String name;
    private String street;
    private String city;
    private String postalCode;
    private String number;
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

    public String getStreet(){return street;}

    public void setStreet(String street) {this.street = street; }

    public String getCity(){return city;}

    public void setCity(String city) {this.city = city; }

    public String getPostalCode(){return postalCode;}

    public void setPostalCode(String postalCode) {this.postalCode = postalCode; }

    public String getNumber(){return number;}

    public void setNumber(String number) {this.number = number; }

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
        this.street = b.getStreet();
        this.city = b.getCity();
        this.postalCode = b.getPostalCode();
        this.number = b.getNumber();
        this.latitude = b.getLatitude().doubleValue();
        this.longitude = b.getLongitude().doubleValue();
    }

    public static BuildingDto make(Building b) {
        BuildingDto dto = new BuildingDto();
        dto.wrap(b);
        return dto;
    }
}
