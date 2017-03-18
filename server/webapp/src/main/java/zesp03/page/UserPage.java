package zesp03.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.Database;
import zesp03.data.row.UserRow;
import zesp03.entity.User;
import zesp03.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Controller
public class UserPage {
    @GetMapping(value = "/user")
    public String get(
            @RequestParam(value="id") long id,
            ModelMap model) {
        UserRow user;
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = em.find(User.class, id);
            if(u == null)
                throw new NotFoundException("user");
            user = new UserRow(u);
            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        model.put("selected", user);
        return "user";
    }
}
