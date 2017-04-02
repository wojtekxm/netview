package zesp03.webapp.dto;

public class ExamineResultDto {
    private long controllerId;
    private int updatedDevices;

    public long getControllerId() {
        return controllerId;
    }

    public void setControllerId(long controllerId) {
        this.controllerId = controllerId;
    }

    public int getUpdatedDevices() {
        return updatedDevices;
    }

    public void setUpdatedDevices(int updatedDevices) {
        this.updatedDevices = updatedDevices;
    }
}
