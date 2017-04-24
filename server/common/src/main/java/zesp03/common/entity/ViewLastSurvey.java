package zesp03.common.entity;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "view_last_survey")
public class ViewLastSurvey {
    @Id
    @Column(name = "frequency_id", nullable = false)
    private Long frequencyId;

    @Column(name = "survey_id", nullable = true)
    private Long surveyId;

    @Column(name = "\"timestamp\"", nullable = true)
    private Integer timestamp;

    @Column(name = "is_enabled", nullable = true)
    private Boolean enabled;

    @Column(name = "clients_sum", nullable = true)
    private Integer clientsSum;

    @Column(name = "deleted", nullable = true)
    private Boolean deleted;

    public Long getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(Long frequencyId) {
        this.frequencyId = frequencyId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getEnabled() {
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
