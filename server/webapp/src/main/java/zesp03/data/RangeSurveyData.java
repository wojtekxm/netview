package zesp03.data;

public class RangeSurveyData {
    private long deviceId;
    private long timeStart;
    private long timeEnd;
    private long totalSum;
    private int min;
    private int max;
    private long surveyRange;

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public long getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(long totalSum) {
        this.totalSum = totalSum;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public long getSurveyRange() {
        return surveyRange;
    }

    public void setSurveyRange(long surveyRange) {
        this.surveyRange = surveyRange;
    }
}
