/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//TODO equals
@Entity
@Table(name = "building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 20, unique = true, nullable = false)
    private String code;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String street;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(name = "postal_code", length = 6, nullable = false)
    private String postalCode;

    @Column(length = 10, nullable = false)
    private String number;

    @Column(precision=8, scale=6)
    private BigDecimal latitude;

    @Column(precision=8, scale=6)
    private BigDecimal longitude;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY)
    private List<Controller> controllerList = new ArrayList<>();

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY)
    private List<Device> deviceList = new ArrayList<>();

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY)
    private List<LinkUnitBuilding> lubList = new ArrayList<>();

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

    public String getStreet(){return street;}

    public void setStreet(String street) {this.street = street; }

    public String getCity(){return city;}

    public void setCity(String city) {this.city = city; }

    public String getPostalCode(){return postalCode;}

    public void setPostalCode(String postalCode) {this.postalCode = postalCode; }

    public String getNumber(){return number;}

    public void setNumber(String number) {this.number = number; }

    public Number getLatitude() {return latitude; }

    public void setLatitude(BigDecimal latitude) {this.latitude = latitude; }

    public Number getLongitude() { return longitude; }

    public void setLongitude(BigDecimal longitude) {this.longitude = longitude;}

    public List<Controller> getControllerList() {
        return controllerList;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public List<LinkUnitBuilding> getLubList() {
        return lubList;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
