package zesp03.entity;

import javax.persistence.*;

@Entity
@Table(name = "device_survey")
public class DeviceSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "device_survey")
    @TableGenerator(name = "device_survey", pkColumnValue = "device_survey")
    private Long id;

    @Column(name = "\"timestamp\"", nullable = false)
    private Integer timestamp;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;

    @Column(name = "clients_sum", nullable = false)
    private Integer clientsSum;

    @ManyToOne
    @JoinColumn(
            name = "device_id",
            foreignKey = @ForeignKey(name = "devicesurvey_device_fk")
    )
    private Device device;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Integer getClientsSum() {
        return clientsSum;
    }

    public void setClientsSum(Integer clientsSum) {
        this.clientsSum = clientsSum;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
