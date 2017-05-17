package zesp03.service.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class ResponsiveShutdown {
    private static final Logger log = LoggerFactory.getLogger(ResponsiveShutdown.class);

    private final AtomicBoolean shouldStop;

    public ResponsiveShutdown() {
        shouldStop = new AtomicBoolean(false);
        Thread t = new Thread( () -> {
            shouldStop.set(true);
            log.debug("killed");
        } );
        Runtime.getRuntime().addShutdownHook(t);
    }

    public boolean shouldStop() {
        return shouldStop.get();
    }
}
