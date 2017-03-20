package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.core.Database;
import zesp03.common.entity.Device;
import zesp03.common.exception.NotFoundException;
import zesp03.webapp.data.row.ControllerRow;
import zesp03.webapp.data.row.DeviceRow;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Controller
public class DevicePage {
    @GetMapping("/device")
    public String getDevice(
            @RequestParam("id") long id,
            ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Device d = em.find(Device.class, id);
            if(d == null)
                throw new NotFoundException("device");
            DeviceRow drow = new DeviceRow(d);
            ControllerRow crow = new ControllerRow(d.getController());
            model.put("device", drow);
            model.put("controller", crow);
            tran.commit();
            return "device";
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }
}