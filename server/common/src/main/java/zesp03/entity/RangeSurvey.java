package zesp03.entity;

import javax.persistence.*;

@Entity
@Table(name = "range_survey")
public class RangeSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "range_survey")
    @TableGenerator(name = "range_survey", pkColumnValue = "range_survey")
    private Long id;

    @Column(name = "time_start", nullable = false)
    private Long timeStart; // timestamp w sekundach, inclusive

    @Column(name = "time_end", nullable = false)
    private Long timeEnd; // timestamp w sekundach, inclusive

    @Column(name = "time_range", nullable = false)
    private Long timeRange; // różnica czasu między timeEnd a timeStart

    @Column(name = "total_sum", nullable = false)
    private Long totalSum;

    @Column(name = "\"min\"", nullable = false)
    private Integer min;

    @Column(name = "\"max\"", nullable = false)
    private Integer max;

    @Column(name = "survey_range", nullable = false)
    private Long surveyRange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "device_id",
            foreignKey = @ForeignKey(name = "range_survey_device_fk")
    )
    private Device device;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Long timeStart) {
        this.timeStart = timeStart;
    }

    public Long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Long getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(Long timeRange) {
        this.timeRange = timeRange;
    }

    public Long getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Long totalSum) {
        this.totalSum = totalSum;
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

    public Long getSurveyRange() {
        return surveyRange;
    }

    public void setSurveyRange(Long surveyRange) {
        this.surveyRange = surveyRange;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
