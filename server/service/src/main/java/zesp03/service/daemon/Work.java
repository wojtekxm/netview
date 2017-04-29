package zesp03.service.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zesp03.common.core.App;
import zesp03.common.service.ExamineService;

import java.time.Duration;
import java.time.Instant;

@Component
public class Work {
    private static final Logger log = LoggerFactory.getLogger(Daemon.class);
    private boolean shutdown = false;

    @Autowired
    private ExamineService examineService;

    public void work() {
        Thread t = new Thread(() -> shutdown = true);
        Runtime.getRuntime().addShutdownHook(t);
        while(!shutdown) {
            App.reloadCustomProperties();
            final int WAIT_SECONDS = App.getExamineInterval();
            final Instant t0 = Instant.now();
            examineService.examineAll();
            final Instant t1 = Instant.now();
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
