/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.dto.input;

public class ImportFakeSurveysDto {
    private long deviceId;
    private int frequencyMhz;
    private int timeStart;
    private int minInterval;
    private int maxInterval;
    private int maxClients;
    private int numberOfSurveys;

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public int getFrequencyMhz() {
        return frequencyMhz;
    }

    public void setFrequencyMhz(int frequencyMhz) {
        this.frequencyMhz = frequencyMhz;
    }

    public int getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(int timeStart) {
        this.timeStart = timeStart;
    }

    public int getMinInterval() {
        return minInterval;
    }

    public void setMinInterval(int minInterval) {
        this.minInterval = minInterval;
    }

    public int getMaxInterval() {
        return maxInterval;
    }

    public void setMaxInterval(int maxInterval) {
        this.maxInterval = maxInterval;
    }

    public int getMaxClients() {
        return maxClients;
    }

    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }

    public int getNumberOfSurveys() {
        return numberOfSurveys;
    }

    public void setNumberOfSurveys(int numberOfSurveys) {
        this.numberOfSurveys = numberOfSurveys;
    }
}
