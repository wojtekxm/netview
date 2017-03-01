package zesp03.entity;

import javax.persistence.*;

@Entity
@Table(name = "link_unit_building")
public class Link_unit_building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@Column(nullable = false)
    private Long unit_id;

    @Column(nullable = false)
    private Long building_id;
*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "building_id",
            foreignKey = @ForeignKey(name = "building_fk"),
            nullable = false,
            unique = true
    )
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "unit_id",
            foreignKey = @ForeignKey(name = "unit_fk"),
            nullable = false,
            unique = true
    )
    private Unit unit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Building getBuilding(){return building; }

    public void setBuilding(Building building) {this.building = building;}

    public Unit getUnit(){return unit; }

    public void setUnit(Unit unit) {this.unit = unit; }



}
