package zesp03.rest.resource;

import zesp03.core.Database;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("surveys")
@Produces("application/json")
public class SurveysResource {
    public static class ClientsOriginalElement {
        private int timestamp;
        private int clientsSum;

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public int getClientsSum() {
            return clientsSum;
        }

        public void setClientsSum(int clientsSum) {
            this.clientsSum = clientsSum;
        }
    }

    public static class ClientsGroupedElement {
        private double average;
        private int min;
        private int max;
        private int timestampStart;
        private int timestampEnd;

        public double getAverage() {
            return average;
        }

        public void setAverage(double average) {
            this.average = average;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getTimestampStart() {
            return timestampStart;
        }

        public void setTimestampStart(int timestampStart) {
            this.timestampStart = timestampStart;
        }

        public int getTimestampEnd() {
            return timestampEnd;
        }

        public void setTimestampEnd(int timestampEnd) {
            this.timestampEnd = timestampEnd;
        }
    }

    /**
     * Zwraca listę badań dla urządzenia o id równym <code>device</code>.
     * Zwrócona lista będzie posortowana po czasie wykonania badania, rosnąco.
     * Lista może być pusta.
     * Lista będzie zawierać tylko badania których czas spełnia warunek: start <= czas < end.
     *
     * @param device id urządzenia
     * @param start  timestamp w sekundach, dolny limit (włącznie) czasu badań
     * @param end    timestamp w sekundach, górny limit (wyłącznie) czasu badań
     */
    @GET
    @Path("clients-original")
    public List<ClientsOriginalElement> getClientsOriginal(
            @QueryParam("device") long device,
            @QueryParam("start") int start,
            @QueryParam("end") int end) {
        if (start < 0 || end < 0 || end < start)
            throw new BadRequestException();
        List<ClientsOriginalElement> result;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Device d = em.find(Device.class, device);
            if (d == null) throw new NotFoundException();

            result = em.createQuery("SELECT ds FROM DeviceSurvey ds WHERE ds.device.id = :id AND ds.timestamp >= :start AND ds.timestamp < :end ORDER BY ds.timestamp ASC",
                    DeviceSurvey.class)
                    .setParameter("id", device)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList()
                    .stream()
                    .map(ds -> {
                        ClientsOriginalElement coe = new ClientsOriginalElement();
                        coe.setTimestamp(ds.getTimestamp());
                        coe.setClientsSum(ds.getClientsSum());
                        return coe;
                    })
                    .collect(Collectors.toList());

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        return result;
    }

    /**
     * Zwraca pogrupowaną listę badań dla urządzenia o id <code>device</code>.
     * Lista będzie zawierać <code>groupNumber</code> grup (elementów).
     * Jeszcze nie zaimplementowane!
     * Wszystkie grupy będą łącznie pokrywać czas (timestamp) z przedziału [ start; start + groupSpan * groupNumber ).
     *
     * @param device      id urządzenia
     * @param start       timestamp w sekundach, dolny limit (włącznie) czasu badań
     * @param groupSpan   liczba sekund która ma być reprezentowana przez jedną grupę
     * @param groupNumber liczba grup
     */
    @GET
    @Path("clients-grouped")
    public List<ClientsGroupedElement> getClientsGrouped(
            @QueryParam("device") long device,
            @QueryParam("start") int start,
            @QueryParam("group-span") int groupSpan,
            @QueryParam("group-number") int groupNumber) {
        List<ClientsGroupedElement> result = new ArrayList<>();
        //TODO ...
        throw new BadRequestException();//!
    }
}
