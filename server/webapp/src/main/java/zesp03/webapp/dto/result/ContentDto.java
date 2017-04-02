package zesp03.webapp.dto.result;

import java.time.Instant;
import java.util.function.Supplier;

public class ContentDto<T> extends BaseResultDto {
    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public static <T> ContentDto<T> make(Supplier<T> supplier) {
        final Instant t0 = Instant.now();
        final ContentDto<T> result = new ContentDto<>();
        final T content = supplier.get();
        result.setContent(content);
        result.makeQueryTime(t0);
        return result;
    }
}
