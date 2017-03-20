package zesp03.webapp.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.core.Database;
import zesp03.common.entity.*;
import zesp03.common.exception.ValidationException;
import zesp03.webapp.dto.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SearchApi {
    private static final Logger log = LoggerFactory.getLogger(SearchApi.class);

    @GetMapping("/api/search")
    public SearchDto search(
            @RequestParam("q") String query) {
        if(query.length() < 2)
            throw new ValidationException("query", "too short");

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            //TODO pozwolić na szukanie użytkowników tylko rootowi
            //TODO dodać pola jakie typy nas interesują
            //TODO sprawdzić czy query już zawiera % ?
            //TODO sprawdź wydajność SQL, może powinienem zrobić fetch join
            //TODO pretty JSON
            final String begins = query + "%";
            final String anywhere = "%" + query + "%";

            final List<DeviceDto> devices = em.createQuery("SELECT d FROM Device d WHERE " +
                            "d.name LIKE :begins OR d.description LIKE :anywhere",
                    Device.class)
                    .setParameter("begins", begins)
                    .setParameter("anywhere", anywhere)
                    .getResultList()
                    .stream()
                    .map(DeviceDto::make)
                    .collect(Collectors.toList());

            final List<ControllerDto> controllers = em.createQuery("SELECT c FROM Controller c WHERE " +
                            "c.name LIKE :begins OR c.description LIKE :anywhere OR c.ipv4 LIKE :anywhere",
                    Controller.class)
                    .setParameter("begins", begins)
                    .setParameter("anywhere", anywhere)
                    .getResultList()
                    .stream()
                    .map(ControllerDto::make)
                    .collect(Collectors.toList());

            final List<BuildingDto> buildings = em.createQuery("SELECT b FROM Building b WHERE " +
                            "b.code LIKE :begins OR b.name LIKE :anywhere",
                    Building.class)
                    .setParameter("begins", begins)
                    .setParameter("anywhere", anywhere)
                    .getResultList()
                    .stream()
                    .map(BuildingDto::make)
                    .collect(Collectors.toList());

            final List<UnitDto> units = em.createQuery("SELECT u FROM Unit u WHERE " +
                            "u.code LIKE :begins OR u.description LIKE :anywhere",
                    Unit.class)
                    .setParameter("begins", begins)
                    .setParameter("anywhere", anywhere)
                    .getResultList()
                    .stream()
                    .map(UnitDto::make)
                    .collect(Collectors.toList());

            final List<UserDto> users = em.createQuery("SELECT u FROM User u WHERE u.name LIKE :begins",
                    User.class)
                    .setParameter("begins", begins)
                    .getResultList()
                    .stream()
                    .map(UserDto::make)
                    .collect(Collectors.toList());

            final SearchDto result = new SearchDto();
            result.setDevices(devices);
            result.setControllers(controllers);
            result.setBuildings(buildings);
            result.setUnits(units);
            result.setUsers(users);
            tran.commit();
            return result;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }
}
