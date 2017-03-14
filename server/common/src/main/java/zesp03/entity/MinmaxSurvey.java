package zesp03.entity;

import javax.persistence.*;

@Entity
@Table(name = "minmax_survey")
public class MinmaxSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "minmax_survey")
    @TableGenerator(name = "minmax_survey", pkColumnValue = "minmax_survey")
    private Long id;

    @Column(name = "first_survey", nullable = false)
    private Integer firstSurvey; // timestamp w sekundach pierwszego zawartego badania, < lastSurvey

    @Column(name = "last_survey", nullable = false)
    private Integer lastSurvey; // timestamp w sekundach ostatniego zawartego badania, > firstSurvey

    @Column(name = "survey_span", nullable = false)
    private Integer surveySpan;

    @Column(name = "\"min\"", nullable = false)
    private Integer min;

    @Column(name = "\"max\"", nullable = false)
    private Integer max;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFirstSurvey() {
        return firstSurvey;
    }

    public void setFirstSurvey(Integer firstSurvey) {
        this.firstSurvey = firstSurvey;
    }

    public Integer getLastSurvey() {
        return lastSurvey;
    }

    public void setLastSurvey(Integer lastSurvey) {
        this.lastSurvey = lastSurvey;
    }

    public Integer getSurveySpan() {
        return surveySpan;
    }

    public void setSurveySpan(Integer surveySpan) {
        this.surveySpan = surveySpan;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
