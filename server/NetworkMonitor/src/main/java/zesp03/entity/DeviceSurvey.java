package zesp03.entity;

import javax.persistence.*;

@Entity
@Table(name = "device_survey")
public class DeviceSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "device_survey")
    @TableGenerator(name = "device_survey", pkColumnValue = "device_survey", allocationSize = 1)
    private int id;

    @Column(name = "\"timestamp\"", nullable = false)
    private int timestamp;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled;

    @Column(name = "clients_sum", nullable = false)
    private int clientsSum;

    @ManyToOne
    @JoinColumn(
            name = "device_id",
            foreignKey = @ForeignKey(name = "device_survey_ibfk_1")
    )
    private Device device;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getClientsSum() {
        return clientsSum;
    }

    public void setClientsSum(int clientsSum) {
        this.clientsSum = clientsSum;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
