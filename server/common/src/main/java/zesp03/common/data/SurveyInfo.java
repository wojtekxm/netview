/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.data;

public class SurveyInfo {
    private final String name;
    private final int frequencyMhz;
    private final int clientsSum;

    public SurveyInfo(String name, int frequencyMhz, int clientsSum) {
        if(name == null) {
            throw new IllegalArgumentException("name == null");
        }
        this.name = name;
        this.frequencyMhz = frequencyMhz;
        this.clientsSum = clientsSum;
    }

    public String getName() {
        return name;
    }

    public int getFrequencyMhz() {
        return frequencyMhz;
    }

    public int getClientsSum() {
        return clientsSum;
    }
}
