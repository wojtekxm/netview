package zesp03.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.Database;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ChartApi {
    @GetMapping("/api/chart")
    public List<Object[]> getDev(
            @RequestParam("id") long id) {
        List<Object[]> results=new ArrayList<Object[]>();
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();
            results = em.createQuery(" SELECT cs.clientsSum,cs.timestamp FROM DeviceSurvey cs " +
                    "WHERE cs.device.id= :id", Object[].class)
                    .setParameter("id", id)
                    .getResultList();
            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
        return results;
    }
}
