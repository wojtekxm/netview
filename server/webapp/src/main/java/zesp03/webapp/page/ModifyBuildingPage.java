package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.core.Database;
import zesp03.common.exception.NotFoundException;
import zesp03.webapp.dto.BuildingDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Created by Media on 2017-03-23.
 */
@Controller
public class ModifyBuildingPage {
    @GetMapping("/modify-building")
    public String get(
            @RequestParam("id") long id,
            ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            zesp03.common.entity.Building b = em.find(zesp03.common.entity.Building.class, id);
            if(b == null)
                throw new NotFoundException("building");
            BuildingDto dto = BuildingDto.make(b);
            model.put("building", dto);
            tran.commit();
            return "modify-building";
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }
}