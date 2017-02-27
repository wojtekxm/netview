package zesp03.service;

import zesp03.core.App;
import zesp03.core.Database;

import javax.naming.NamingException;
import java.time.Duration;
import java.time.Instant;

public class Daemon {
    private static boolean shutdown = false;

    public static void main(String[] args) throws NamingException {
        Database.init();
        Thread t = new Thread(() -> shutdown = true);
        Runtime.getRuntime().addShutdownHook(t);
        while (!shutdown) {
            final Instant start = Instant.now();
            App.examineNetwork();
            final Instant next = start.plusSeconds(5 * 60);
            final Instant now = Instant.now();
            long millis = Duration.between(now, next).toMillis();
            if (millis < 1) millis = 1;
            try {
                Thread.sleep(millis);
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        }
        Database.destroy();
    }
}
