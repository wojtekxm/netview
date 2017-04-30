package zesp03.service.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zesp03.common.service.GarbageCollectingService;

@Component
public class GarbageCollectionWorker implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(GarbageCollectionWorker.class);

    @Autowired
    private ResponsiveShutdown responsiveShutdown;

    @Autowired
    private GarbageCollectingService garbageCollectingService;

    @Override
    public void run() {
        NextClean next = NextClean.SURVEY;
        while(!responsiveShutdown.shouldStop()) {
            log.debug("next = {}", next);
            switch(next) {
                case SURVEY:
                    if(!garbageCollectingService.cleanSomeSurveys(1000)) {
                        next = NextClean.FREQUENCY;
                    }
                    break;
                case FREQUENCY:
                    if(!garbageCollectingService.cleanSomeFrequencies(1000)) {
                        next = NextClean.DEVICE;
                    }
                    break;
                case DEVICE:
                    if(!garbageCollectingService.cleanSomeDevices(1000)) {
                        next = NextClean.CONTROLLER;
                    }
                    break;
                default:
                    if(!garbageCollectingService.cleanSomeControllers(1000)) {
                        next = NextClean.SURVEY;
                        try {
                            Thread.sleep(1000);
                        }
                        catch(InterruptedException exc) {
                            log.debug("sleep interrupted: {}", exc.getLocalizedMessage());
                        }
                    }
                    break;
            }
        }
        log.info("GarbageCollectionWorker ends");
    }

    private enum NextClean {
        SURVEY, FREQUENCY, DEVICE, CONTROLLER
    }
}
