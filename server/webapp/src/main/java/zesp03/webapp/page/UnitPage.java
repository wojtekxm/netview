package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.core.Database;
import zesp03.common.entity.Building;
import zesp03.common.entity.LinkUnitBuilding;
import zesp03.common.entity.Unit;
import zesp03.common.exception.NotFoundException;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.dto.result.BaseResultDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UnitPage {
    @GetMapping("/unit")
    public String get(
            @RequestParam("id") long id,
            ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            zesp03.common.entity.Unit u = em.find( zesp03.common.entity.Unit.class, id );
            if(u == null)
                throw new NotFoundException("unit");

            List< BuildingDto > b = em.createQuery( "SELECT b " +
                    "FROM LinkUnitBuilding lub " +
                    "INNER JOIN Unit u ON (lub.unit.id = " + id +
                    " AND lub.unit.id = u.id)" +
                    "INNER JOIN Building b ON lub.building.id = b.id", zesp03.common.entity.Building.class)
                    .getResultList()
                    .stream()
                    .map( BuildingDto::make )
                    .collect( Collectors.toList() );

            if(b == null)
                throw new NotFoundException( "buildings" );

            tran.commit();

            model.put( "unit", UnitDto.make(u) );
            model.put( "buildings", b );

            return "unit";

        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

}