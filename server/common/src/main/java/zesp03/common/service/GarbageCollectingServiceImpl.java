package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class GarbageCollectingServiceImpl implements GarbageCollectingService {
    private static final Logger log = LoggerFactory.getLogger(GarbageCollectingServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean cleanSomeSurveys(int maxDeletedRows) {
        if(maxDeletedRows < 1) {
            throw new IllegalArgumentException("maxDeletedRows < 1");
        }
        List<DeviceSurvey> list = em.createQuery(
                "SELECT ds FROM DeviceSurvey ds WHERE ds.deleted = TRUE",
                DeviceSurvey.class)
                .setMaxResults(maxDeletedRows)
                .getResultList();
        int deleted = list.size();
        deleteFromDeviceSurvey(list);
        if(deleted < maxDeletedRows) {
            list = em.createQuery("SELECT ds FROM " +
                            "DeviceSurvey ds LEFT JOIN ds.frequency f " +
                            "WHERE f.deleted = TRUE",
                    DeviceSurvey.class)
                    .setMaxResults(maxDeletedRows - deleted)
                    .getResultList();
            deleted += list.size();
            deleteFromDeviceSurvey(list);
        }
        log.debug("deleted={}", deleted);
        return deleted > 0;
    }

    @Override
    public boolean cleanSomeFrequencies(int maxDeletedRows) {
        if(maxDeletedRows < 1) {
            throw new IllegalArgumentException("maxDeletedRows < 1");
        }
        List<Object[]> list = em.createQuery("SELECT f, COUNT(sur.id) FROM " +
                        "DeviceFrequency f LEFT JOIN f.surveyList sur " +
                        "WHERE f.deleted = TRUE " +
                        "GROUP BY f HAVING COUNT(sur.id) = 0",
                Object[].class)
                .setMaxResults(maxDeletedRows)
                .getResultList();
        if(list.isEmpty()) {
            log.debug("deleted=0");
            return false;
        }
        List<DeviceFrequency> toDelete = new LinkedList<>();
        for(Object[] arr : list) {
            toDelete.add((DeviceFrequency)arr[0]);
        }
        int deleted = toDelete.size();
        deleteFromDeviceFrequency(toDelete);
        log.debug("deleted={}", deleted);
        return deleted > 0;
    }

    @Override
    public boolean cleanSomeDevices(int maxDeletedRows) {
        if(maxDeletedRows < 1) {
            throw new IllegalArgumentException("maxDeletedRows < 1");
        }
        List<Object[]> list = em.createQuery("SELECT dev, COUNT(freq.id) FROM " +
                        "Device dev LEFT JOIN dev.frequencyList freq " +
                        "WHERE dev.deleted = TRUE " +
                        "GROUP BY dev HAVING COUNT(freq.id) = 0",
                Object[].class)
                .setMaxResults(maxDeletedRows)
                .getResultList();
        if(list.isEmpty()) {
            log.debug("deleted=0");
            return false;
        }
        List<Device> toDelete = new LinkedList<>();
        for(Object[] arr : list) {
            toDelete.add((Device)arr[0]);
        }
        int deleted = toDelete.size();
        deleteFromDevice(toDelete);
        log.debug("deleted={}", deleted);
        return deleted > 0;
    }

    @Override
    public boolean cleanSomeControllers(int maxDeletedRows) {
        if(maxDeletedRows < 1) {
            throw new IllegalArgumentException("maxDeletedRows < 1");
        }
        List<Object[]> list = em.createQuery("SELECT con, COUNT(dev.id) FROM " +
                        "Controller con LEFT JOIN con.deviceList dev " +
                        "WHERE con.deleted = TRUE " +
                        "GROUP BY con HAVING COUNT(dev.id) = 0",
                Object[].class)
                .setMaxResults(maxDeletedRows)
                .getResultList();
        if(list.isEmpty()) {
            log.debug("deleted=0");
            return false;
        }
        List<Controller> toDelete = new LinkedList<>();
        for(Object[] arr : list) {
            toDelete.add((Controller)arr[0]);
        }
        int deleted = toDelete.size();
        deleteFromController(toDelete);
        log.debug("deleted={}", deleted);
        return deleted > 0;
    }

    private void deleteFromDeviceSurvey(Collection<DeviceSurvey> col) {
        if(col.size() < 1)return;
        final Set<Long> set = col.stream()
                .map(DeviceSurvey::getId)
                .collect(Collectors.toSet());
        em.createQuery("DELETE FROM DeviceSurvey ds WHERE ds.id IN (:ids)")
                .setParameter("ids", set)
                .executeUpdate();
    }

    private void deleteFromDeviceFrequency(Collection<DeviceFrequency> col) {
        if(col.size() < 1)return;
        final Set<Long> set = col.stream()
                .map(DeviceFrequency::getId)
                .collect(Collectors.toSet());
        em.createQuery("DELETE FROM DeviceFrequency df WHERE df.id IN (:ids)")
                .setParameter("ids", set)
                .executeUpdate();
    }

    private void deleteFromDevice(Collection<Device> col) {
        if(col.size() < 1)return;
        final Set<Long> set = col.stream()
                .map(Device::getId)
                .collect(Collectors.toSet());
        em.createQuery("DELETE FROM Device dev WHERE dev.id IN (:ids)")
                .setParameter("ids", set)
                .executeUpdate();
    }

    private void deleteFromController(Collection<Controller> col) {
        if(col.size() < 1)return;
        final Set<Long> set = col.stream()
                .map(Controller::getId)
                .collect(Collectors.toSet());
        em.createQuery("DELETE FROM Controller c WHERE c.id IN (:ids)")
                .setParameter("ids", set)
                .executeUpdate();
    }
}
