package zesp03.entity;

import javax.persistence.*;

@Entity
@Table(name = "current_survey")
public class CurrentSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "current_survey")
    @TableGenerator(name = "current_survey", pkColumnValue = "current_survey")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "device_id",
            foreignKey = @ForeignKey(name = "currentsurvey_device_fk"),
            nullable = false
    )
    private Device device;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "survey_id",
            foreignKey = @ForeignKey(name = "currentsurvey_devicesurvey_fk")
    )
    private DeviceSurvey survey;

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

    public DeviceSurvey getSurvey() {
        return survey;
    }

    public void setSurvey(DeviceSurvey survey) {
        this.survey = survey;
    }
}
