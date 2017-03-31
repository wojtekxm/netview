package zesp03.common.data;

import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;

import java.util.ArrayList;
import java.util.List;

public class CurrentDeviceState {
    private final Device device;
    private final List<FrequencySurvey> list = new ArrayList<>();

    // f i s mogą być null
    public CurrentDeviceState(Device d, DeviceFrequency f, DeviceSurvey s) {
        FrequencySurvey fs = validate(d, f, s);
        this.device = d;
        if(fs.frequency != null) {
            list.add(fs);
        }
    }

    public Device getDevice() {
        return device;
    }

    // zwraca null jak nie znajdzie
    public DeviceFrequency findFrequency(Integer frequencyMhz) {
        for(FrequencySurvey fs : list) {
            if( fs.frequency.getFrequency().equals(frequencyMhz) ) {
                return fs.frequency;
            }
        }
        return null;
    }

    // zwraca null jak nie znajdzie
    public DeviceSurvey findSurvey(Integer frequencyMhz) {
        for(FrequencySurvey fs : list) {
            if( fs.frequency.getFrequency().equals(frequencyMhz) ) {
                return fs.survey;
            }
        }
        return null;
    }

    public void merge(CurrentDeviceState c) {
        if( ! c.device.getId().equals( device.getId() ) ) {
            throw new IllegalArgumentException("Device");
        }
        if(c.list == list) {
            throw new IllegalArgumentException("list");
        }
        for(FrequencySurvey fs : c.list) {
            list.add(fs);
        }
    }

    private static FrequencySurvey validate(Device d, DeviceFrequency f, DeviceSurvey s) {
        if(d == null) {
            throw new IllegalArgumentException("Device");
        }
        if(f != null) {
            if( ! f.getDevice().getId().equals( d.getId() ) ) {
                throw new IllegalArgumentException("DeviceFrequency");
            }
            if(s != null) {
                if( ! s.getFrequency().getId().equals( f.getId() ) ) {
                    throw new IllegalArgumentException("DeviceSurvey");
                }
            }
            return new FrequencySurvey(f, s);
        }
        return new FrequencySurvey(f, s);
    }

    public static class FrequencySurvey {
        private final DeviceFrequency frequency;
        private final DeviceSurvey survey;
        public FrequencySurvey(DeviceFrequency f, DeviceSurvey s) {
            this.frequency = f;
            this.survey = s;
        }
    }
}
