package zesp03.entity;

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

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DeviceSurvey> deviceSurveys = new ArrayList<>();

    @OneToOne(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CurrentSurvey currentSurvey;

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

    public List<DeviceSurvey> getDeviceSurveys() {
        return deviceSurveys;
    }

    public void addDeviceSurvey(DeviceSurvey s) {
        deviceSurveys.add(s);
        s.setDevice(this);
    }

    public void removeDeviceSurvey(DeviceSurvey s) {
        if( deviceSurveys.remove(s) )
            s.setDevice(null);
    }

    public void addCurrentSurvey(CurrentSurvey s) {
        this.currentSurvey = s;
        s.setDevice(this);
    }

    public void removeCurrentSurvey() {
        if (currentSurvey != null) {
            currentSurvey.setDevice(null);
            currentSurvey = null;
        }
    }

    public CurrentSurvey getCurrentSurvey() {
        return currentSurvey;
    }
}