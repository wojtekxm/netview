package zesp03.service.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zesp03.common.core.Config;
import zesp03.common.service.ExamineService;

@Component
public class ExamineWorker implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Daemon.class);

    @Autowired
    private ResponsiveShutdown responsiveShutdown;

    @Autowired
    private ExamineService examineService;

    @Override
    public void run() {
        long lastTime = 0L;
        while(!responsiveShutdown.shouldStop()) {
            Config.forceReloadCustomProperties();
            final long nextTime = lastTime + Config.getExamineInterval() * 1000L;
            final long now = System.currentTimeMillis();
            if(now >= nextTime) {
                examineService.examineAll();
                lastTime = now;
            }
            else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException exc) {
                    log.debug("sleep interrupted: {}", exc.getLocalizedMessage());
                }
            }
        }
        log.info("ExamineWorker ends");
    }
}
