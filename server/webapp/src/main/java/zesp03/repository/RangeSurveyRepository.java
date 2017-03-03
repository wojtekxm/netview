package zesp03.repository;

import zesp03.common.Database;
import zesp03.data.RangeSurveyData;
import zesp03.entity.Device;
import zesp03.entity.RangeSurvey;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RangeSurveyRepository {
    /**
     *
     * @param deviceId id urządzenia
     * @param timeStart timestamp w sekundach, inclusive
     * @param timeEnd timestamp w sekundach, inclusive
     * @return null jeśli urządzenie o wskazanym id nie istnieje, lub jeśli nie ma badań dla danego urządzenia ze wskazanego okresu.
     */
    public RangeSurveyData rangeSurvey(long deviceId, long timeStart, long timeEnd) {
        if(timeStart > timeEnd)
            throw new IllegalArgumentException();
        RangeSurveyData result;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Device device = em.find(Device.class, deviceId);
            if(device == null)return null;
            long timeBegin = timeStart;

            result = new RangeSurveyData();
            result.setTimeStart(timeStart);
            result.setTimeEnd(timeEnd);
            result.setDeviceId( device.getId() );
            for(;;) {
                List<RangeSurvey> list = em.createQuery("SELECT rs FROM RangeSurvey rs WHERE rs.device = :device AND " +
                                "rs.timeStart >= :t0 AND rs.timeEnd <= :t1 ORDER BY rs.timeStart ASC, rs.surveyRange DESC",
                        RangeSurvey.class)
                        .setParameter("device", device)
                        .setParameter("t0", timeBegin)
                        .setParameter("t1", timeEnd)
                        .getResultList();
                if(list.isEmpty())break;
                RangeSurvey fragment = list.get(0);
                if(result.getSurveyRange() < 1) {
                    result.setTotalSum( fragment.getTotalSum() );
                    result.setMin( fragment.getMin() );
                    result.setMax( fragment.getMax() );
                    result.setSurveyRange( fragment.getSurveyRange() );
                }
                else {
                    result.setTotalSum( result.getTotalSum() + fragment.getTotalSum() );
                    result.setMin( Integer.min( result.getMin(), fragment.getMin() ) );
                    result.setMax( Integer.max( result.getMax(), fragment.getMax() ) );
                    result.setSurveyRange( result.getSurveyRange() + fragment.getSurveyRange() );
                }
                timeBegin = fragment.getTimeEnd() + 1;
            }

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (tran != null && tran.isActive()) tran.rollback();//???
            if (em != null) em.close();
        }

        return result;
    }
}
