package zesp03.rest.resource;

import zesp03.core.Database;
import zesp03.data.DeviceData;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;
import java.util.List;

@Path("/device")
@Produces("application/json")
public class DeviceResource {
    @GET
    public DeviceData getDevice(@QueryParam("id") long id) {
        DeviceData result = null;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            //TODO sprawdź wydajność SQL
            List<Object[]> list = em.createQuery("SELECT c, d, ds, cs FROM CurrentSurvey cs " +
                    "INNER JOIN cs.survey ds " +
                    "INNER JOIN ds.device d " +
                    "INNER JOIN d.controller c " +
                    "WHERE d.id = :id", Object[].class)
                    .setParameter("id", id)
                    .getResultList();
            if (!list.isEmpty()) {
                Object[] arr = list.get(0);
                Device d = (Device) arr[1];
                DeviceSurvey ds = (DeviceSurvey) arr[2];
                result = new DeviceData(d, ds);
            }

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        if (result == null)
            throw new NotFoundException();
        return result;
    }
}
