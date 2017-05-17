package zesp03.service.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zesp03.common.core.Config;
import zesp03.common.service.GarbageCollectingService;

@Component
public class GarbageCollectionWorker implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(GarbageCollectionWorker.class);

    private ResponsiveShutdown responsiveShutdown;

    @Autowired
    private GarbageCollectingService garbageCollectingService;

    public void setResponsiveShutdown(ResponsiveShutdown responsiveShutdown) {
        this.responsiveShutdown = responsiveShutdown;
    }

    @Override
    public void run() {
        long lastTime = 0L;
        NextClean next = NextClean.SURVEY;
        while(!responsiveShutdown.shouldStop()) {
            Config.forceReloadCustomProperties();
            final long nextTime = lastTime + Config.getDatabaseCleaningInterval() * 1000L;
            final long now = System.currentTimeMillis();
            if(now >= nextTime) {
                switch (next) {
                    case SURVEY:
                        if (!garbageCollectingService.cleanSomeSurveys(1000)) {
                            next = NextClean.FREQUENCY;
                        }
                        break;
                    case FREQUENCY:
                        if (!garbageCollectingService.cleanSomeFrequencies(1000)) {
                            next = NextClean.DEVICE;
                        }
                        break;
                    case DEVICE:
                        if (!garbageCollectingService.cleanSomeDevices(1000)) {
                            next = NextClean.CONTROLLER;
                        }
                        break;
                    case CONTROLLER:
                        if (!garbageCollectingService.cleanSomeControllers(1000)) {
                            next = NextClean.SURVEY;
                            lastTime = now;
                        }
                        break;
                }
            }
            else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException exc) {
                    log.debug("sleep interrupted: {}", exc.getLocalizedMessage());
                }
            }
        }
        log.info("GarbageCollectionWorker ends");
    }

    private enum NextClean {
        SURVEY, FREQUENCY, DEVICE, CONTROLLER
    }
}
