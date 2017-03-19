package zesp03.service.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zesp03.common.service.SurveyService;

import java.time.Duration;
import java.time.Instant;

@Component
public class Work {
    private static final Logger log = LoggerFactory.getLogger(Daemon.class);
    private boolean shutdown = false;

    @Autowired
    SurveyService surveyService;

    public void work() {
        Thread t = new Thread(() -> shutdown = true);
        Runtime.getRuntime().addShutdownHook(t);
        while(!shutdown) {
            final Instant start = Instant.now();
            surveyService.examineAll();
            final int WAIT_SECONDS = 10;
            long sleep = Duration.between(Instant.now(), start.plusSeconds(WAIT_SECONDS)).toMillis();
            if (sleep < 1) sleep = 1;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException exc) {
                log.error("sleep interrupted", exc);
            }
        }
    }
}
