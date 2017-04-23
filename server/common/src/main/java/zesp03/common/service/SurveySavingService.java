package zesp03.common.service;

import zesp03.common.data.ShortSurvey;

import java.util.List;

public interface SurveySavingService {
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

    @Deprecated
    void markTest(Long deviceId, int frequencyMhz, int validAfter);
    @Deprecated
    void justDelete(Long deviceId, int frequencyMhz, int validAfter);
    @Deprecated
    void selectAndDelete(Long deviceId, int frequencyMhz, int validAfter);
}
