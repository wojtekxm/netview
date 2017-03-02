package zesp03.repository;

import zesp03.common.Database;
import zesp03.data.DeviceData;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceRepository {
    public List<DeviceData> checkDevices() {
        List<DeviceData> result;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            //TODO sprawdź wydajność SQL
            //TODO może left join
            result = em.createQuery("SELECT c, d, ds, cs FROM CurrentSurvey cs " +
                    "INNER JOIN cs.survey ds " +
                    "INNER JOIN ds.device d " +
                    "INNER JOIN d.controller c", Object[].class)
                    .getResultList()
                    .stream()
                    .map(arr -> new DeviceData(
                            (Device) arr[1],
                            (DeviceSurvey) arr[2]
                    ))
                    .collect(Collectors.toList());

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        return result;
    }
}
