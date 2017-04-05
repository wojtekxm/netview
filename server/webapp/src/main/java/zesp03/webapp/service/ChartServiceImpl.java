package zesp03.webapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Deprecated
@Service
@Transactional
public class ChartServiceImpl implements ChartService {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> getDev(long id) {
        return em.createQuery(" SELECT cs.clientsSum,cs.timestamp FROM DeviceSurvey cs " +
                        "WHERE cs.frequency.device.id= :id "
                /* AND (cs(timestamp) BETWEEN :timestamp1 AND  :timestamp2 */
                , Object[].class)
                .setParameter("id", id)
                /*.setParameter("timestamp1", timestamp1)
                .setParameter("timestamp2", timestamp2)*/
                .getResultList();
    }
}
