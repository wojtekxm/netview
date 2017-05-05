package zesp03.common.data;

public class SampleAvgMinMax {
    private int timeStart;
    private int timeEnd;
    private double average;
    private int min;
    private int max;
    private int surveySpan;

    public int getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(int timeStart) {
        this.timeStart = timeStart;
    }

    public int getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(int timeEnd) {
        this.timeEnd = timeEnd;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
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

    public int getSurveySpan() {
        return surveySpan;
    }

    public void setSurveySpan(int surveySpan) {
        this.surveySpan = surveySpan;
    }
}
