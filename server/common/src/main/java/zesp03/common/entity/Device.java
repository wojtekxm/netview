package zesp03.common.entity;

import javax.persistence.*;

@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "device")
    @TableGenerator(name = "device", pkColumnValue = "device")
    private Long id;

    @Column(name = "\"name\"", length = 85, nullable = false, unique = true)
    private String name;

    @Column(name = "is_known", nullable = false)
    private Boolean known;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "controller_id",
            foreignKey = @ForeignKey(name = "device_controller_fk")
    )
    private Controller controller;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isKnown() {
        return known;
    }

    public void setKnown(Boolean known) {
        this.known = known;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}