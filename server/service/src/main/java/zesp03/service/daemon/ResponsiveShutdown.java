package zesp03.service.daemon;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ResponsiveShutdown {
    private final AtomicBoolean shouldStop;

    public ResponsiveShutdown() {
        shouldStop = new AtomicBoolean(false);
        Thread t = new Thread( () -> shouldStop.set(true) );
        Runtime.getRuntime().addShutdownHook(t);
    }

    public boolean shouldStop() {
        return shouldStop.get();
    }
}
