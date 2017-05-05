package zesp03.common.data;

import java.util.List;

public class RangeSamples<T> {
    private SampleRaw before;
    private List<T> list;
    private SampleRaw after;

    public SampleRaw getBefore() {
        return before;
    }

    public void setBefore(SampleRaw before) {
        this.before = before;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public SampleRaw getAfter() {
        return after;
    }

    public void setAfter(SampleRaw after) {
        this.after = after;
    }
}
