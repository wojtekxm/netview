package zesp03.common.data;

import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CurrentDeviceState {
    private final Device device;
    private final HashMap<Integer, FrequencySurvey> map = new HashMap<>();

    // f i s mogą być null
    public CurrentDeviceState(Device d, DeviceFrequency f, DeviceSurvey s) {
        FrequencySurvey fs = validate(d, f, s);
        this.device = d;
        if(fs.frequency != null) {
            map.put(fs.frequency.getFrequency(), fs);
        }
    }

    public Device getDevice() {
        return device;
    }

    // zwraca null jak nie znajdzie
    public DeviceFrequency findFrequency(Integer frequencyMhz) {
        FrequencySurvey fs = map.get(frequencyMhz);
        if(fs != null)return fs.frequency;
        return null;
    }

    // zwraca null jak nie znajdzie
    public DeviceSurvey findSurvey(Integer frequencyMhz) {
        FrequencySurvey fs = map.get(frequencyMhz);
        if(fs != null)return fs.survey;
        return null;
    }

    public List<Integer> getFrequencies() {
        return new ArrayList<>(map.keySet());
    }

    public void merge(CurrentDeviceState c) {
        if( ! c.device.getId().equals( device.getId() ) ) {
            throw new IllegalArgumentException("Device");
        }
        if(c.map == map) {
            throw new IllegalArgumentException("map");
        }
        c.map.forEach( (i, fs) -> map.put(i, fs) );
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
        else if(s != null) {
            throw new IllegalArgumentException("DeviceSurvey");
        }
        else {
            return new FrequencySurvey(null, null);
        }
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
