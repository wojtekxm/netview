package zesp03.common.entity;

import javax.persistence.*;

@Entity
@Table(name = "controller")
public class Controller {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "controller")
    @TableGenerator(name = "controller", pkColumnValue = "controller")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "building_id",
            foreignKey = @ForeignKey(name = "building_fk"),
            nullable = false
    )
    private Building building;

    @Column(name = "\"name\"", unique = true, nullable = false, length = 85)
    private String name;

    @Column(nullable = false, length = 15)
    private String ipv4;

    @Column(length = 1000)
    private String description;

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

    public Building getBuilding(){return building; }

    public void setBuilding(Building building) {this.building = building;}
}