package zesp03.common.data;

import java.util.List;

public class RangeSamples {
    private SampleRaw before;
    private List<SampleRaw> list;
    private SampleRaw after;

    public SampleRaw getBefore() {
        return before;
    }

    public void setBefore(SampleRaw before) {
        this.before = before;
    }

    public List<SampleRaw> getList() {
        return list;
    }

    public void setList(List<SampleRaw> list) {
        this.list = list;
    }

    public SampleRaw getAfter() {
        return after;
    }

    public void setAfter(SampleRaw after) {
        this.after = after;
    }
}
