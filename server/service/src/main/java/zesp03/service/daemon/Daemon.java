package zesp03.service.daemon;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import zesp03.common.core.App;
import zesp03.common.core.Database;

public class Daemon {
    public static void main(String[] args) {
        App.runFlyway();
        Database.init();
        AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext();
        acac.scan("zesp03.common.repository", "zesp03.common.service", "zesp03.service.daemon");
        acac.refresh();
        acac.getBean(Work.class).work();
        Database.destroy();
    }
}
