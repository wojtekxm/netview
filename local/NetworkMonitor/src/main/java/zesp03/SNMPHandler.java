package zesp03;

import java.util.List;

/**
 * Ten interfejs służy do obsługi protokołu SNMP.
 * Realizuje zapytania SNMP, przetwarza odpowiedzi i zwraca wyniki w przyjaznym formacie.
 */
public interface SNMPHandler {
    /**
     * Zapytuje kontroler o wskazanym IP o listę wszystkich urządzeń którymi zarządza.
     * Dla każdego urządzenia zwraca strukturę DeviceInfo,
     * czyli nazwa urządzenia, stan (czy jest włączone) i liczba podłączonych klientów.
     * Nie ma takiej gwarancji że nazwy wszystkich urządzeń na tej liście będą unikalne.
     * @param controllerIP adres IP kontrolera
     * @return lista zawierająca informacje o wszystkich zarządzanych urządzeniach
     * @throws SNMPException nie udało się połączyć z kontrolerem o wskazanym IP,
     * lub z innych powodów nie udało się zrealizować zapytania SNMP
     */
    List<DeviceInfo> queryDevices(String controllerIP) throws SNMPException;
}
