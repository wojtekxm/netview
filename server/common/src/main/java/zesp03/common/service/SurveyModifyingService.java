package zesp03.common.service;

import zesp03.common.data.ShortSurvey;
import zesp03.common.data.SurveyInfoUniqueNameFrequency;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SurveyModifyingService {
    /**
     * @return liczba zaktualizowanych urządzeń (liczba nowych zapisanych badań)
     */
    int examineAll();

    /**
     * Wykonuje badanie wskazanego kontrolera.
     * @param controllerId id kontrolera
     * @return liczba zaktualizowanych urządzeń (liczba nowych zapisanych badań)
     */
    int examineOne(long controllerId);

    /**
     * Usuwa wszystkie badania dla danego urządzenia ze wskazaną częstotliwością
     * i dodaje nowe na podstawie podanej listy danych.
     * Jeśli urządzenie nie ma takiej częstotliwości, to ją dodaje.
     * Spreparowane badania mogą być w dowolnej kolejności (nie muszą być posortowane po czasie).
     */
    void importSurveys(Long deviceId, Integer frequencyMhz, List<ShortSurvey> data);

    /**
     * Dla każdego elementu z <code>surveys</code> wstawia do bazy nowe urządzenie i częstotliwość,
     * jeśli takie nie istnieją.
     * @return mapa [nazwa urządzenia => urządzenie z bazy]
     */
    Map<String, Device> makeDevices(Collection<SurveyInfoUniqueNameFrequency> surveys, Controller controller);
}
