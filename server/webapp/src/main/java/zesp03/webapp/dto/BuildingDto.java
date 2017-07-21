/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.dto;

import zesp03.common.entity.Building;

import java.math.BigDecimal;

public class BuildingDto {
    private long id;
    private String code;
    private String name;
    private String street;
    private String number;
    private String postalCode;
    private String city;
    private BigDecimal latitude;
    private BigDecimal longitude;

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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
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
        this.latitude = (BigDecimal) b.getLatitude();//.doubleValue();
        this.longitude = (BigDecimal) b.getLongitude();
    }

    public static BuildingDto make(Building b) {
        BuildingDto dto = new BuildingDto();
        dto.wrap(b);
        return dto;
    }
}
