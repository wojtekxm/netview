package zesp03.common.service;

import zesp03.common.data.SampleRaw;
import zesp03.common.data.SurveyInfo;
import zesp03.common.entity.Controller;
import zesp03.common.util.SurveyInfoCollection;

import java.util.List;

public interface SurveyModifyingService {
    int update(Controller controller, SurveyInfoCollection collection);

    /**
     * Dla każdego elementu z <code>surveys</code> wstawia do bazy nowe urządzenie i częstotliwość,
     * jeśli takie nie istnieją.
     */
    void makeDevices(Controller controller, Iterable<SurveyInfo> surveys);

    /**
     * Usuwa wszystkie badania dla danego urządzenia ze wskazaną częstotliwością
     * i dodaje nowe na podstawie podanej listy danych.
     * Jeśli urządzenie nie ma takiej częstotliwości, to ją dodaje.
     * Spreparowane badania mogą być w dowolnej kolejności (nie muszą być posortowane po czasie).
     */
    void importSurveys(Long deviceId, Integer frequencyMhz, List<SampleRaw> data);

    void deleteForAll();

    void deleteForAll(int before);

    void deleteForOneDevice(Long deviceId);

    void deleteForOneDevice(Long deviceId, int before);
}