package zesp03.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.common.App;
import zesp03.common.Database;
import zesp03.common.SNMPException;

import java.time.Duration;
import java.time.Instant;

public class Daemon {
    private static final Logger log = LoggerFactory.getLogger(Daemon.class);

    private static boolean shutdown = false;

    public static void main(String[] args) {
        App.runFlyway();
        Database.init();
        Thread t = new Thread(() -> shutdown = true);
        Runtime.getRuntime().addShutdownHook(t);
        work2();
        Database.destroy();
    }

    public static void work1() {
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
    }

    public static void work2() {
        for(int t = 0; t < 1; t++) {
            if(shutdown)break;
            try {
                log.info("t={}", t);
                App.examineOne(8);
                Thread.sleep(1000);
            }
            catch(SNMPException exc) {
                log.error("SNMP error");
            }
            catch(InterruptedException exc) {
                log.error("sleep interrupted", exc);
            }
        }
    }
}
