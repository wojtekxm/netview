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
        while(!responsiveShutdown.shouldStop()) {
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException exc) {
                log.debug("sleep interrupted: {}", exc.getLocalizedMessage());
            }
        }
    }
}
