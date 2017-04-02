package zesp03.webapp.dto;

import zesp03.common.data.CurrentDeviceState;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceSurvey;

import java.util.HashMap;
import java.util.Map;

public class CurrentDeviceStateDto {
    private long id;
    private String name;
    private boolean known;
    private String description;
    private long controllerId;
    private Map<Integer, ShortSurveyDto> frequencySurvey;

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

    public long getControllerId() {
        return controllerId;
    }

    public void setControllerId(long controllerId) {
        this.controllerId = controllerId;
    }

    public Map<Integer, ShortSurveyDto> getFrequencySurvey() {
        return frequencySurvey;
    }

    public void setFrequencySurvey(Map<Integer, ShortSurveyDto> frequencySurvey) {
        this.frequencySurvey = frequencySurvey;
    }

    public void wrap(CurrentDeviceState c) {
        Device d = c.getDevice();
        this.id = d.getId();
        this.name = d.getName();
        this.description = d.getDescription();
        this.known = d.isKnown();
        this.controllerId = d.getController().getId();
        this.frequencySurvey = new HashMap<>();
        for(Integer freq : c.getFrequencies() ) {
            DeviceSurvey survey = c.findSurvey(freq);
            if(survey != null) {
                ShortSurveyDto dto = ShortSurveyDto.make(survey);
                frequencySurvey.put(freq, dto);
            }
            else {
                frequencySurvey.put(freq, null);
            }
        }
    }

    public static CurrentDeviceStateDto make(CurrentDeviceState c) {
        CurrentDeviceStateDto dto = new CurrentDeviceStateDto();
        dto.wrap(c);
        return dto;
    }
}
