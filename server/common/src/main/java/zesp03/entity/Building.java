package zesp03.entity;

import javax.persistence.*;
import java.math.BigDecimal;

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



}
