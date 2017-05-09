package zesp03.common.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "device")
    @TableGenerator(name = "device", pkColumnValue = "device")
    private Long id;

    @Column(name = "\"name\"", length = 85, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "controller_id", nullable = true)
    private Controller controller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = true)
    private Building building;

    @Column(name = "deleted", nullable = false)
    private Long deleted;

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY)
    private List<DeviceFrequency> frequencyList = new ArrayList<>();

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

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Long getDeleted() {
        return deleted;
    }

    public void setDeleted(Long deleted) {
        this.deleted = deleted;
    }

    public List<DeviceFrequency> getFrequencyList() {
        return frequencyList;
    }

    public void setFrequencyList(List<DeviceFrequency> frequencyList) {
        this.frequencyList = frequencyList;
    }
}