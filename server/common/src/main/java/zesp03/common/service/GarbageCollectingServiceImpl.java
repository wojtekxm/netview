package zesp03.common.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GarbageCollectingServiceImpl implements GarbageCollectingService {
    @Override
    public boolean cleanSomeSurveys(int maxDeletedRows) {
        return false;//TODO...
    }

    @Override
    public boolean cleanSomeFrequencies(int maxDeletedRows) {
        return false;//TODO...
    }

    @Override
    public boolean cleanSomeDevices(int maxDeletedRows) {
        return false;//TODO...
    }

    @Override
    public boolean cleanSomeControllers(int maxDeletedRows) {
        return false;//TODO...
    }
}
