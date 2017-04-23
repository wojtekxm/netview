package zesp03.common.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "device_frequency")
public class DeviceFrequency {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "device_frequency")
    @TableGenerator(name = "device_frequency", pkColumnValue = "device_frequency")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "frequency_mhz", nullable = false)
    private Integer frequency;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "frequency", fetch = FetchType.LAZY)
    private List<DeviceSurvey> surveyList = new ArrayList<>();

    public DeviceFrequency() {
        this.deleted = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<DeviceSurvey> getSurveyList() {
        return surveyList;
    }

    public void setSurveyList(List<DeviceSurvey> surveyList) {
        this.surveyList = surveyList;
    }
}
