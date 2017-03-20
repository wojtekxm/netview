package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import zesp03.common.core.Database;
import zesp03.common.entity.User;
import zesp03.webapp.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AllUsersPage {
    @GetMapping("/all-users")
    public String get(ModelMap model) {
        final ArrayList<UserDto> allUsers = new ArrayList<>();

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List<User> list = em.createQuery("SELECT u FROM User u", User.class)
                    .getResultList();
            for (User u : list) {
                allUsers.add(UserDto.make(u));
            }

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        model.put("list", allUsers);
        return "all-users";
    }
}
