package zesp03.service;

import zesp03.common.Database;
import zesp03.data.DeviceStateData;
import zesp03.entity.Controller;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceService {
    //!?
    public List<DeviceStateData> checkAll(EntityManager em) {
        return convert(
                em.createQuery("SELECT dev, sur FROM DeviceSurvey sur " +
                                "RIGHT JOIN sur.device dev " +
                                "LEFT JOIN FETCH dev.controller WHERE sur.timestamp = (" +
                                "SELECT MAX(s.timestamp) FROM DeviceSurvey s WHERE s.device = dev)",
                        Object[].class)
                        .getResultList()
        );
    }

    //!?
    public List<DeviceStateData> checkSome(Collection<Long> ids, EntityManager em) {
        return convert(
                em.createQuery("SELECT dev, sur FROM DeviceSurvey sur " +
                                "RIGHT JOIN sur.device dev " +
                                "LEFT JOIN FETCH dev.controller WHERE sur.timestamp = (" +
                                "SELECT MAX(s.timestamp) FROM DeviceSurvey s WHERE s.device = dev) AND " +
                                "dev.id IN (:ids)",
                        Object[].class)
                        .setParameter("ids", ids)
                        .getResultList()
        );
    }

    //!?
    public List<DeviceStateData> checkAll() {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List<DeviceStateData> r = checkAll(em);
            tran.commit();
            return r;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    //!?
    public List<DeviceStateData> checkSome(Collection<Long> ids) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List<DeviceStateData> r = checkSome(ids, em);
            tran.commit();
            return r;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    private List<DeviceStateData> convert(List<Object[]> list) {
        return list
                .stream()
                .map( arr -> {
                    Device dev = (Device)arr[0];
                    DeviceSurvey sur = (DeviceSurvey)arr[1];
                    Controller con = dev.getController();
                    DeviceStateData dsd = new DeviceStateData();
                    dsd.setDevice(dev);
                    dsd.setSurvey(sur);
                    dsd.setController(con);
                    return dsd;
                } )
                .collect(Collectors.toList());
    }
}