package zesp03.webapp.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.core.Database;
import zesp03.common.entity.Controller;
import zesp03.common.exception.NotFoundException;
import zesp03.common.exception.ValidationException;
import zesp03.common.util.IPv4;
import zesp03.webapp.data.row.ControllerRow;
import zesp03.webapp.dto.BaseResultDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ControllerApi {
    @GetMapping("/api/all-controllers")
    public List<ControllerRow> getAllControllers() {
        List<ControllerRow> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT c FROM Controller c", Controller.class)
                    .getResultList()
                    .stream()
                    .map(ControllerRow::new)
                    .collect(Collectors.toList());

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        return list;
    }

    @GetMapping("/api/controller")
    public ControllerRow getController(
            @RequestParam("id") long id) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller c = em.find(Controller.class, id);
            if(c == null)
                throw new NotFoundException("controller");
            ControllerRow r = new ControllerRow(c);
            tran.commit();
            return r;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @PostMapping(value = "/api/remove-controller", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto remove(
            @RequestParam("id") long id) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller c = em.find(Controller.class, id);
            if (c == null)
                throw new NotFoundException("controller");
            em.remove(c);
            tran.commit();
            return new BaseResultDto(true);
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @PostMapping(value = "/api/create-controller", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto create(
            @RequestParam("name") String name,
            @RequestParam("ipv4") String ipv4,
            @RequestParam(value = "description", required = false) String description) {
        if(!IPv4.isValid(ipv4))
            throw new ValidationException("ipv4", "invalid format");
        //TODO co jeśli taki kontroler już istnieje?
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller controller = new Controller();
            controller.setName(name);
            controller.setIpv4(ipv4);
            controller.setDescription(description);
            //TODO jaki budynek przypisać do tego kontrolera?
            em.persist(controller);
            tran.commit();
            return new BaseResultDto(true);
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }
}
