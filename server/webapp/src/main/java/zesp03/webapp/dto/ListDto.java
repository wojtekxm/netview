package zesp03.webapp.dto;


import java.util.List;

public class ListDto<T> extends BaseResultDto {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
