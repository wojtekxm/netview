/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.dto.input;

import java.math.BigDecimal;

public class CreateBuildingDto {
    private String code;
    private String name;
    private String street;
    private String city;
    private String postalCode;
    private String number;
    private BigDecimal latitude;
    private BigDecimal longitude;

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
}
