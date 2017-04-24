package zesp03.common.service;

public interface GarbageCollectingService {
    /**
     * @return true jeśli cokolwiek zostało usunięte
     */
    boolean cleanSomeSurveys(int maxDeletedRows);

    /**
     * @return true jeśli cokolwiek zostało usunięte
     */
    boolean cleanSomeFrequencies(int maxDeletedRows);

    /**
     * @return true jeśli cokolwiek zostało usunięte
     */
    boolean cleanSomeDevices(int maxDeletedRows);

    /**
     * @return true jeśli cokolwiek zostało usunięte
     */
    boolean cleanSomeControllers(int maxDeletedRows);
}
