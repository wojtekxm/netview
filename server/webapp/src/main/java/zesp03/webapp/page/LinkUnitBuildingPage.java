package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import zesp03.common.core.Database;
import zesp03.common.entity.LinkUnitBuilding;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@Controller
public class LinkUnitBuildingPage {
    @GetMapping("/building")
    public String get(ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;


        List<LinkUnitBuilding> BuildingList;

        try {

            em = Database.createEntityManager();
            tran = em.getTransaction();

            tran.begin();

            BuildingList = em.createQuery("SELECT lub FROM LinkUnitBuilding lub", LinkUnitBuilding.class).getResultList();
            tran.commit();

        } catch (RuntimeException exc) {
            if( tran != null && tran.isActive() )
                tran.rollback();
            throw exc;
        } finally {
            if (em != null)
                em.close();
        }

        model.put("list", BuildingList);
        return "link-unit-building";
    }
}
