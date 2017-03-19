package zesp03.common.service;

import zesp03.common.data.SurveyInfo;

import java.util.List;

/**
 * Ten interfejs służy do obsługi protokołu SNMP.
 * Realizuje zapytania SNMP, przetwarza odpowiedzi i zwraca wyniki w przyjaznym formacie.
 */
public interface NetworkService {
    /**
     * Zapytuje kontroler o wskazanym IP o listę wszystkich urządzeń którymi zarządza.
     * Dla każdego urządzenia zwraca strukturę SurveyInfo,
     * czyli nazwa urządzenia, stan (czy jest włączone) i liczba podłączonych klientów.
     * Nie ma takiej gwarancji że nazwy wszystkich urządzeń na tej liście będą unikalne.
     *
     * @param controllerIP adres IP kontrolera
     * @return lista zawierająca informacje o wszystkich zarządzanych urządzeniach
     */
    List<SurveyInfo> queryDevices(String controllerIP);
}
