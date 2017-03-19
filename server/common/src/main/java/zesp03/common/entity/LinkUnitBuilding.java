package zesp03.common.entity;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Entity
@Table(name = "link_unit_building")
@Proxy(lazy = false)
public class LinkUnitBuilding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "link_unit_building")
    @TableGenerator(name = "link_unit_building", pkColumnValue = "link_unit_building")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "building_id",
            foreignKey = @ForeignKey(name = "building_fk"),
            nullable = false
    )
    private Building building;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "unit_id",
            foreignKey = @ForeignKey(name = "unit_fk"),
            nullable = false

    )
    private Unit unit;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Building getBuilding(){return building; }

    public void setBuilding(Building building) {this.building = building;}

    public Unit getUnit(){return unit; }

    public void setUnit(Unit unit) {this.unit = unit; }



}
