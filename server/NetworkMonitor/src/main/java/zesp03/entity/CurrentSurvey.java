package zesp03.entity;

import javax.persistence.*;

@Entity
@Table(name = "current_survey")
public class CurrentSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "current_survey")
    @TableGenerator(name = "current_survey", pkColumnValue = "current_survey")
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "device_id",
            foreignKey = @ForeignKey(name = "currentsurvey_device_fk")
    )
    private Device device;

    @ManyToOne
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
