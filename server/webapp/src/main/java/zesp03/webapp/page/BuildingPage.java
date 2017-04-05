package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.core.Database;
import zesp03.common.entity.Unit;
import zesp03.common.exception.NotFoundException;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.UnitDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BuildingPage {
    @GetMapping("/building")
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

            List<UnitDto> u = em.createQuery( "SELECT u " +
                    "FROM LinkUnitBuilding lub " +
                    "INNER JOIN Building b ON (lub.building.id = " + id +
                    " AND lub.building.id = b.id)" +
                    "INNER JOIN Unit u ON lub.unit.id = u.id", zesp03.common.entity.Unit.class)
                    .getResultList()
                    .stream()
                    .map( UnitDto::make )
                    .collect( Collectors.toList() );

            if(u == null)
                throw new NotFoundException( "units" );


            model.put( "building", BuildingDto.make(b) );
            model.put( "units", u );

            tran.commit();
            return "building";
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }
}
