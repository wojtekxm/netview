package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.core.Database;
import zesp03.common.exception.NotFoundException;
import zesp03.webapp.dto.UnitDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


@Controller
public class ModifyUnitPage {
        @GetMapping("/modify-unit")
        public String get(
                @RequestParam("id") long id,
                ModelMap model) {
            EntityManager em = null;
            EntityTransaction tran = null;
            try {
                em = Database.createEntityManager();
                tran = em.getTransaction();
                tran.begin();

                zesp03.common.entity.Unit u = em.find(zesp03.common.entity.Unit.class, id);
                if(u == null)
                    throw new NotFoundException("unit");
                UnitDto dto = UnitDto.make(u);
                model.put("unit", dto);
                tran.commit();
                return "modify-unit";
            } catch (RuntimeException exc) {
                if (tran != null && tran.isActive()) tran.rollback();
                throw exc;
            } finally {
                if (em != null) em.close();
            }
        }
}
