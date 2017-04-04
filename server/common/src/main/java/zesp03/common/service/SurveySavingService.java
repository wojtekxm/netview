package zesp03.common.service;

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
}
