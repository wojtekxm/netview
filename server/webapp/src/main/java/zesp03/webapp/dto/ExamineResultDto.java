package zesp03.webapp.dto;

public class ExamineResultDto extends BaseResultDto {
    private long controllerId;
    private double timeElapsed; // w sekundach
    private int updatedDevices;

    public ExamineResultDto(boolean success) {
        super(success);
    }

    public long getControllerId() {
        return controllerId;
    }

    public void setControllerId(long controllerId) {
        this.controllerId = controllerId;
    }

    public double getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(double timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public int getUpdatedDevices() {
        return updatedDevices;
    }

    public void setUpdatedDevices(int updatedDevices) {
        this.updatedDevices = updatedDevices;
    }
}