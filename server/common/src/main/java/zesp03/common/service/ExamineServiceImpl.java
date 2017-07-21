package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zesp03.common.data.SurveyInfo;
import zesp03.common.entity.Controller;
import zesp03.common.exception.NotFoundException;
import zesp03.common.exception.SNMPException;
import zesp03.common.repository.ControllerRepository;
import zesp03.common.util.SurveyInfoCollection;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.*;

@Service
public class ExamineServiceImpl implements ExamineService {
    private static final Logger log = LoggerFactory.getLogger(ExamineServiceImpl.class);

    @Autowired
    private NetworkService fakeNetworkService;

    @Autowired
    private NetworkService realNetworkService;

    @Autowired
    private ControllerRepository controllerRepository;

    @Autowired
    private SurveyModifyingService surveyModifyingService;

    private final ExecutorService executorService;

    public ExamineServiceImpl() {
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void examineAll() {
        final Instant t0 = Instant.now();
        Iterable<Controller> list = controllerRepository.findAllNotDeleted();
        final List<ExamineTask> tasks = new ArrayList<>();
        for (Controller c : list) {
            tasks.add(new ExamineTask(c));
        }
        try {
            List<Future<Integer>> results = executorService.invokeAll(tasks);
            int updated = 0;
            for(Future<Integer> fut : results) {
                updated += fut.get();
            }
            final Instant t1 = Instant.now();
            final double elapsed = Duration.between(t0, t1).toMillis() * 0.001;
            log.info("examineAll() has finished in {} seconds with {} new surveys",
                    String.format(Locale.US, "%.3f", elapsed), updated);
        }
        catch(InterruptedException | ExecutionException exc) {
            final Instant t1 = Instant.now();
            final double elapsed = Duration.between(t0, t1).toMillis() * 0.001;
            log.warn("examineAll() has been working for {} seconds before being cancelled",
                    String.format(Locale.US, "%.3f", elapsed));
            log.warn("cancelled by an exception", exc);
        }
    }

    @Override
    public void examineOne(long controllerId) {
        final Instant t0 = Instant.now();

        Optional<Controller> opt = controllerRepository.findOneNotDeleted(controllerId);
        if(!opt.isPresent()) {
            throw new NotFoundException("controller");
        }
        final ExamineTask task = new ExamineTask(opt.get());
        final Future<Integer> fut = executorService.submit(task);
        try {
            final int updated = fut.get();
            final Instant t1 = Instant.now();
            final double elapsed = Duration.between(t0, t1).toMillis() * 0.001;
            log.info("examineOne() has finished in {} seconds with {} new surveys",
                    String.format(Locale.US, "%.3f", elapsed), updated);
        }
        catch(InterruptedException | ExecutionException exc) {
            final Instant t1 = Instant.now();
            final double elapsed = Duration.between(t0, t1).toMillis() * 0.001;
            log.warn("examineOne() has been working for {} seconds before being cancelled",
                    String.format(Locale.US, "%.3f", elapsed));
            log.warn("cancelled by an exception", exc);
        }
    }

    private class ExamineTask implements Callable<Integer> {
        final Controller controller;

        ExamineTask(Controller c) {
            this.controller = c;
        }

        @Override
        public Integer call() {
            List<SurveyInfo> originalSurveys;
            try {
                final NetworkService ser = controller.isFake() ? fakeNetworkService : realNetworkService;
                originalSurveys = ser.queryDevices(controller.getIpv4(), controller.getCommunity());
            }
            catch(SNMPException exc) {
                log.warn("could not examine controller (id={}, ipv4={}): {}", controller.getId(), controller.getIpv4(), exc.getLocalizedMessage());
                return 0;
            }
            final SurveyInfoCollection col = new SurveyInfoCollection(originalSurveys);
            return surveyModifyingService.update(controller, col);
        }
    }
}
