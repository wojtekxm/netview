package zesp03.common.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Column(precision=8, scale=6)
    private BigDecimal latitude;

    @Column(precision=8, scale=6)
    private BigDecimal longitude;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY)
    private List<Controller> controllerList = new ArrayList<>();

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

    public Number getLatitude() {return latitude; }

    public void setLatitude(BigDecimal latitude) {this.latitude = latitude; }

    public Number getLongitude() { return longitude; }

    public void setLongitude(BigDecimal longitude) {this.longitude = longitude;}

    public List<Controller> getControllerList() {
        return controllerList;
    }

    public void setControllerList(List<Controller> controllerList) {
        this.controllerList = controllerList;
    }

    public List<LinkUnitBuilding> getLubList() {
        return lubList;
    }

    public void setLubList(List<LinkUnitBuilding> lubList) {
        this.lubList = lubList;
    }
}
