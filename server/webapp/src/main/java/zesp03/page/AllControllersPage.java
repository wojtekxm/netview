package zesp03.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import zesp03.common.Database;
import zesp03.data.row.ControllerRow;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AllControllersPage {
    @GetMapping("/all-controllers")
    public String get(ModelMap model) {
        final ArrayList<ControllerRow> list = new ArrayList<>();

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List<zesp03.entity.Controller> controllers = em.createQuery("SELECT c FROM Controller c",
                    zesp03.entity.Controller.class)
                    .getResultList();
            for (zesp03.entity.Controller c : controllers) {
                list.add(new ControllerRow(c));
            }

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        model.put("list", list);
        return "all-controllers";
    }
}
