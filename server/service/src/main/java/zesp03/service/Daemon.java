package zesp03.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.common.App;
import zesp03.common.Database;

import javax.naming.NamingException;
import java.time.Duration;
import java.time.Instant;

public class Daemon {
    private static final Logger log = LoggerFactory.getLogger(Daemon.class);

    private static boolean shutdown = false;

    public static void main(String[] args) throws NamingException {
        App.runFlyway();
        Database.init();
        Thread t = new Thread(() -> shutdown = true);
        Runtime.getRuntime().addShutdownHook(t);
        while (!shutdown) {
            final Instant start = Instant.now();
            App.examineAll();
            long sleep = Duration.between(Instant.now(), start.plusSeconds(5 * 60)).toMillis();
            if (sleep < 1) sleep = 1;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException exc) {
                log.error("sleep interrupted", exc);
            }
        }
        Database.destroy();
    }
}
