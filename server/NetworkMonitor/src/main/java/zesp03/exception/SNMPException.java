package zesp03.exception;

/**
 * wyjątek sygnalizuje że nie da się zrealizować operacji związanej z SNMP
 */
public class SNMPException extends Exception {
    public SNMPException(String message) {
        super(message);
    }

    public SNMPException(String message, Throwable cause) {
        super(message, cause);
    }
}