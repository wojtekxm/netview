package zesp03.webapp.dto;

import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.SampleRaw;
import zesp03.common.entity.Building;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceSurvey;

import java.util.HashMap;
import java.util.Map;

public class DeviceDetailsDto {
    private long id;
    private String name;
    private ControllerDto controller;
    private BuildingDto building;
    private Map<Integer, SampleRaw> frequency;

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

    public ControllerDto getController() {
        return controller;
    }

    public void setController(ControllerDto controller) {
        this.controller = controller;
    }

    public BuildingDto getBuilding() {
        return building;
    }

    public void setBuilding(BuildingDto building) {
        this.building = building;
    }

    public Map<Integer, SampleRaw> getFrequency() {
        return frequency;
    }

    public void setFrequency(Map<Integer, SampleRaw> frequency) {
        this.frequency = frequency;
    }

    public void wrap(CurrentDeviceState cds) {
        final Device dev = cds.getDevice();
        final Controller con = dev.getController();
        final Building b = dev.getBuilding();
        this.id = dev.getId();
        this.name = dev.getName();
        if(con != null) {
            this.controller = ControllerDto.make(con);
        }
        else {
            this.controller = null;
        }
        if(b != null) {
            this.building = BuildingDto.make(b);
        }
        else {
            this.building = null;
        }
        this.frequency = new HashMap<>();
        for(Integer freq : cds.getFrequencies() ) {
            DeviceSurvey survey = cds.findSurvey(freq);
            if(survey != null) {
                SampleRaw dto = SampleRaw.make(survey);
                this.frequency.put(freq, dto);
            }
            else {
                this.frequency.put(freq, null);
            }
        }
    }

    public static DeviceDetailsDto make(CurrentDeviceState s) {
        DeviceDetailsDto dto = new DeviceDetailsDto();
        dto.wrap(s);
        return dto;
    }
}
