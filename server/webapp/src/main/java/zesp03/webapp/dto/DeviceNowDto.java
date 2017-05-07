package zesp03.webapp.dto;

import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.SampleRaw;
import zesp03.common.entity.Building;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceSurvey;

import java.util.HashMap;
import java.util.Map;

public class DeviceNowDto {
    private long id;
    private String name;
    private String description;
    private Long controllerId;
    private Long buildingId;
    private Map<Integer, SampleRaw> frequencySurvey;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getControllerId() {
        return controllerId;
    }

    public void setControllerId(Long controllerId) {
        this.controllerId = controllerId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Map<Integer, SampleRaw> getFrequencySurvey() {
        return frequencySurvey;
    }

    public void setFrequencySurvey(Map<Integer, SampleRaw> frequencySurvey) {
        this.frequencySurvey = frequencySurvey;
    }

    public void wrap(CurrentDeviceState cds) {
        final Device dev = cds.getDevice();
        final Controller con = dev.getController();
        final Building b = dev.getBuilding();
        this.id = dev.getId();
        this.name = dev.getName();
        this.description = dev.getDescription();
        if(con != null) {
            this.controllerId = con.getId();
        }
        else {
            this.controllerId = null;
        }
        if(b != null) {
            this.buildingId = b.getId();
        }
        else {
            this.buildingId = null;
        }
        this.frequencySurvey = new HashMap<>();
        for(Integer freq : cds.getFrequencies() ) {
            DeviceSurvey survey = cds.findSurvey(freq);
            if(survey != null) {
                SampleRaw dto = SampleRaw.make(survey);
                frequencySurvey.put(freq, dto);
            }
            else {
                frequencySurvey.put(freq, null);
            }
        }
    }

    public static DeviceNowDto make(CurrentDeviceState c) {
        DeviceNowDto dto = new DeviceNowDto();
        dto.wrap(c);
        return dto;
    }
}
