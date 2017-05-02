package zesp03.common.service;

import zesp03.common.data.ShortSurvey;
import zesp03.common.data.SurveyInfo;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.util.SurveyInfoCollection;

import java.util.List;
import java.util.Map;

public interface SurveyModifyingService {
    int update(Controller controller, SurveyInfoCollection collection);

    /**
     * Dla każdego elementu z <code>surveys</code> wstawia do bazy nowe urządzenie i częstotliwość,
     * jeśli takie nie istnieją.
     * @return mapa [nazwa urządzenia => urządzenie z bazy]
     */
    Map<String, Device> makeDevices(Controller controller, Iterable<SurveyInfo> surveys);

    /**
     * Usuwa wszystkie badania dla danego urządzenia ze wskazaną częstotliwością
     * i dodaje nowe na podstawie podanej listy danych.
     * Jeśli urządzenie nie ma takiej częstotliwości, to ją dodaje.
     * Spreparowane badania mogą być w dowolnej kolejności (nie muszą być posortowane po czasie).
     */
    void importSurveys(Long deviceId, Integer frequencyMhz, List<ShortSurvey> data);
}