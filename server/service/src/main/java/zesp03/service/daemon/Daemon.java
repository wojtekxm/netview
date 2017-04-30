package zesp03.service.daemon;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zesp03.common.core.App;

@Configuration
public class Daemon {
    @Bean
    public ResponsiveShutdown responsiveShutdown() {
        return new ResponsiveShutdown();
    }

    public static void main(String[] args) {
        App.runFlyway();
        AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext();
        acac.scan("zesp03.common.repository", "zesp03.common.service", "zesp03.service.daemon");
        acac.refresh();
        acac.getBean(ExamineWorker.class).run();
    }
}
