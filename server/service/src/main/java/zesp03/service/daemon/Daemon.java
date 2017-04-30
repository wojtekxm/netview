package zesp03.service.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zesp03.common.core.App;

@Configuration
public class Daemon {
    private static final Logger log = LoggerFactory.getLogger(Daemon.class);

    @Bean
    public ResponsiveShutdown responsiveShutdown() {
        return new ResponsiveShutdown();
    }

    public static void main(String[] args) {
        App.runFlyway();
        AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext();
        acac.scan("zesp03.common.repository", "zesp03.common.service", "zesp03.service.daemon");
        acac.refresh();
        ExamineWorker ew = acac.getBean(ExamineWorker.class);
        GarbageCollectionWorker gcw = acac.getBean(GarbageCollectionWorker.class);
        Thread te = new Thread(ew);
        Thread tgc = new Thread(gcw);
        te.start();
        tgc.start();
        try {
            te.join();
        }
        catch(InterruptedException exc) {
            log.warn("join on ExamineWorker interrupted", exc);
        }
        try {
            tgc.join();
        }
        catch(InterruptedException exc) {
            log.warn("join on GarbageCollectionWorker interrupted", exc);
        }
        log.info("bye");
    }
}
