/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
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

    private ResponsiveShutdown responsiveShutdown;

    @Autowired
    private ExamineService examineService;

    public void setResponsiveShutdown(ResponsiveShutdown responsiveShutdown) {
        this.responsiveShutdown = responsiveShutdown;
    }

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
