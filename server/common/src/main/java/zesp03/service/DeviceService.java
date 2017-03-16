package zesp03.service;

import zesp03.common.Database;
import zesp03.data.DeviceNow;
import zesp03.entity.DeviceSurvey;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeviceService {
    public List<DeviceNow> checkAll() {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List<DeviceNow> r = checkAll(em);
            tran.rollback();
            return r;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }
    public List<DeviceNow> checkAll(EntityManager em) {
        List list = em.createNativeQuery("SELECT device.id AS devid,\n" +
                "device.name AS devname,\n" +
                "device.description AS devdesc,\n" +
                "device.is_known AS devknown,\n" +
                "device.controller_id AS devcid,\n" +
                "ds.id AS dsid,\n" +
                "ds.`timestamp` AS dstime,\n" +
                "ds.is_enabled AS dsen,\n" +
                "ds.clients_sum AS dscli,\n" +
                "ds.device_id AS dsdid,\n" +
                "ds.cumulative AS dscum FROM\n" +
                "device LEFT JOIN (\n" +
                "SELECT ds.id, ds.`timestamp`, ds.is_enabled, ds.clients_sum, ds.device_id, ds.cumulative FROM\n" +
                "device_survey ds INNER JOIN (\n" +
                "SELECT device_id, MAX(`timestamp`) AS m FROM device_survey GROUP BY device_id\n" +
                ") best ON ds.device_id = best.device_id AND ds.timestamp = best.m\n" +
                ") ds ON device.id = ds.device_id")
                .getResultList();
        ArrayList<DeviceNow> result = new ArrayList<>();
        for(Object obj : list) {
            Object[] arr = (Object[])obj;
            result.add( fromRow(arr) );
        }
        return result;
    }

    public List<DeviceNow> checkSome(Collection<Long> ids, EntityManager em) {
        List list = em.createNativeQuery("SELECT device.id AS devid,\n" +
                "device.name AS devname,\n" +
                "device.description AS devdesc,\n" +
                "device.is_known AS devknown,\n" +
                "device.controller_id AS devcid,\n" +
                "ds.id AS dsid,\n" +
                "ds.`timestamp` AS dstime,\n" +
                "ds.is_enabled AS dsen,\n" +
                "ds.clients_sum AS dscli,\n" +
                "ds.device_id AS dsdid,\n" +
                "ds.cumulative AS dscum FROM\n" +
                "device LEFT JOIN (\n" +
                "SELECT ds.id, ds.`timestamp`, ds.is_enabled, ds.clients_sum, ds.device_id, ds.cumulative FROM\n" +
                "device_survey ds INNER JOIN (\n" +
                "SELECT device_id, MAX(`timestamp`) AS m FROM device_survey GROUP BY device_id\n" +
                ") best ON ds.device_id = best.device_id AND ds.timestamp = best.m\n" +
                ") ds ON device.id = ds.device_id WHERE device.id IN (:ids)")
                .setParameter("ids", ids)
                .getResultList();
        ArrayList<DeviceNow> result = new ArrayList<>();
        for(Object obj : list) {
            Object[] arr = (Object[])obj;
            result.add( fromRow(arr) );
        }
        return result;
    }

    private static DeviceNow fromRow(Object[] row) {
        final DeviceNow d = new DeviceNow();
        d.setId( getLongOrNull(row[0]) );
        d.setName( (String)row[1] );
        d.setDescription( (String)row[2] );
        d.setKnown( (Boolean)row[3] );
        d.setControllerId( getLongOrNull(row[4]) );
        final Long sid = getLongOrNull(row[5]);
        if(sid != null) {
            final DeviceSurvey s = new DeviceSurvey();
            s.setId( sid );
            s.setTimestamp( getIntegerOrNull(row[6]) );
            s.setEnabled( (Boolean)row[7] );
            s.setClientsSum( getIntegerOrNull(row[8]) );
            s.setCumulative( getLongOrNull(row[9]) );
            d.setSurvey(s);
        }
        d.setSurvey(null);
        return d;
    }

    private static Long getLongOrNull(Object o) {
        if(o == null)return null;
        else return ((Number)o).longValue();
    }

    private static Integer getIntegerOrNull(Object o) {
        if(o == null)return null;
        else return ((Number)o).intValue();
    }
}