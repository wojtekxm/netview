package zesp03.webapp.dto;

import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.ShortSurvey;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceSurvey;

import java.util.HashMap;
import java.util.Map;

public class DeviceDetailsDto {
    private long id;
    private String name;
    private boolean known;
    private String description;
    private ControllerDto controller;
    private Map<Integer, ShortSurvey> frequency;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isKnown() {
        return known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ControllerDto getController() {
        return controller;
    }

    public void setController(ControllerDto controller) {
        this.controller = controller;
    }

    public Map<Integer, ShortSurvey> getFrequency() {
        return frequency;
    }

    public void setFrequency(Map<Integer, ShortSurvey> frequency) {
        this.frequency = frequency;
    }

    public void wrap(CurrentDeviceState s, Controller c) {
        final Device d = s.getDevice();
        this.id = d.getId();
        this.name = d.getName();
        this.description = d.getDescription();
        this.known = d.isKnown();
        this.controller = ControllerDto.make(c);
        this.frequency = new HashMap<>();
        for(Integer freq : s.getFrequencies() ) {
            DeviceSurvey survey = s.findSurvey(freq);
            if(survey != null) {
                ShortSurvey dto = ShortSurvey.make(survey);
                this.frequency.put(freq, dto);
            }
            else {
                this.frequency.put(freq, null);
            }
        }
    }

    public static DeviceDetailsDto make(CurrentDeviceState s, Controller c) {
        DeviceDetailsDto dto = new DeviceDetailsDto();
        dto.wrap(s, c);
        return dto;
    }
}
