/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
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

    @Column(name = "frequency_mhz", nullable = false)
    private Integer frequencyMhz;

    @Column(name = "\"timestamp\"", nullable = true)
    private Integer timestamp;

    @Column(name = "is_enabled", nullable = true)
    private Boolean enabled;

    @Column(name = "clients_sum", nullable = true)
    private Integer clientsSum;

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

    public Integer getFrequencyMhz() {
        return frequencyMhz;
    }

    public void setFrequencyMhz(Integer frequencyMhz) {
        this.frequencyMhz = frequencyMhz;
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
}
