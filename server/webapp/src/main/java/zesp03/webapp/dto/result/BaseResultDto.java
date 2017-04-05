package zesp03.webapp.dto.result;

import java.time.Duration;
import java.time.Instant;

public class BaseResultDto {
    private boolean success;
    private String error; // często jest nullem
    private Double queryTime; // w sekundach, może być null

    public BaseResultDto() {
        this.success = true;
    }

    public BaseResultDto(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Double getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Double queryTime) {
        this.queryTime = queryTime;
    }

    public void makeQueryTime(Instant since) {
        queryTime = Duration.between(since, Instant.now()).toNanos() * 0.000000001;
    }

    public static BaseResultDto make(Runnable action) {
        final Instant t0 = Instant.now();
        final BaseResultDto result = new BaseResultDto();
        action.run();
        result.makeQueryTime(t0);
        return result;
    }
}
