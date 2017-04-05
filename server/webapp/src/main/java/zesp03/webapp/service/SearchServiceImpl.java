package zesp03.webapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.entity.*;
import zesp03.common.exception.ValidationException;
import zesp03.webapp.dto.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SearchServiceImpl implements SearchService {
    @PersistenceContext
    private EntityManager em;

    @Override
    public SearchDto search(String query) {
        if(query == null || query.length() < 1) {
            throw new ValidationException("query", "too short");
        }

        //TODO pozwolić na szukanie użytkowników tylko rootowi
        //TODO dodać pola jakie typy nas interesują
        //TODO sprawdzić czy query już zawiera % ?
        //TODO sprawdź wydajność SQL, może powinienem zrobić fetch join
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
        return result;
    }
}
