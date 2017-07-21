/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
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
