package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.core.Database;
import zesp03.common.exception.NotFoundException;
import zesp03.webapp.data.row.ControllerRow;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Controller
public class ControllerPage {
    @GetMapping("/controller")
    public String get(
            @RequestParam("id") long id,
            ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            zesp03.common.entity.Controller c = em.find(zesp03.common.entity.Controller.class, id);
            if(c == null)
                throw new NotFoundException("controller");
            ControllerRow row = new ControllerRow(c);
            model.put("controller", row);
            tran.commit();
            return "controller";
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }
}
