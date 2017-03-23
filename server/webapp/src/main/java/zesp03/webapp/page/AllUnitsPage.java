package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import zesp03.common.core.Database;
import zesp03.common.entity.Unit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@Controller
public class AllUnitsPage {
    @GetMapping("/all-units")
    public String get(ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;


        List<Unit> UnitList;

        try {

            em = Database.createEntityManager();
            tran = em.getTransaction();

            tran.begin();

            UnitList = em.createQuery("SELECT u FROM Unit u", Unit.class).getResultList();
            tran.commit();

        } catch (RuntimeException exc) {
            if( tran != null && tran.isActive() )
                tran.rollback();
            throw exc;
        } finally {
            if (em != null)
                em.close();
        }

        model.put("list", UnitList);
        return "all-units";
    }
}
