package zesp03.service.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zesp03.common.service.SurveySavingService;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

@Component
public class Work {
    private static final Logger log = LoggerFactory.getLogger(Daemon.class);
    private boolean shutdown = false;

    @Autowired
    private SurveySavingService surveySavingService;

    public void work() {
        Thread t = new Thread(() -> shutdown = true);
        Runtime.getRuntime().addShutdownHook(t);
        while(!shutdown) {
            final int WAIT_SECONDS = 10;
            final Instant t0 = Instant.now();
            final int updatedDevices = surveySavingService.examineAll();
            final Instant t1 = Instant.now();
            double elapsed = Duration.between(t0, t1).toMillis() * 0.001;
            log.info("network survey of all devices finished, {} new surveys, {} seconds elapsed",
                    updatedDevices, String.format(Locale.US, "%.3f", elapsed));
            long sleep = Duration.between(t1, t0.plusSeconds(WAIT_SECONDS)).toMillis();
            if (sleep < 1) sleep = 1;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException exc) {
                log.error("sleep interrupted", exc);
            }
        }
    }
}
