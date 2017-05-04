package zesp03.common.entity;

import javax.persistence.*;

@Entity
@Table(name = "device_survey")
public class DeviceSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "device_survey")
    @TableGenerator(name = "device_survey", pkColumnValue = "device_survey")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frequency_id", nullable = false)
    private DeviceFrequency frequency;

    @Column(name = "\"timestamp\"", nullable = false)
    private Integer timestamp;

    @Column(name = "is_enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "clients_sum", nullable = false)
    private Integer clientsSum;

    @Column(name = "deleted", nullable = false)
    private Long deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeviceFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(DeviceFrequency frequency) {
        this.frequency = frequency;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getClientsSum() {
        return clientsSum;
    }

    public void setClientsSum(Integer clientsSum) {
        this.clientsSum = clientsSum;
    }

    public Long getDeleted() {
        return deleted;
    }

    public void setDeleted(Long deleted) {
        this.deleted = deleted;
    }
}
