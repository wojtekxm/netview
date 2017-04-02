package zesp03.webapp.dto.result;

public class ContentDto<T> extends BaseResultDto {
    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
