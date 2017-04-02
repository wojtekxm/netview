package zesp03.common.service;

public interface DeviceFrequencyService {
    Long getFrequencyIdOrThrow(Long deviceId, Integer frequencyMhz);
}
