package zesp03;

/**
 * Plain Old Java Object
 * Ta klasa jest używana przez interfejs SNMPHandler do zwracania informacji o urządzeniu.
 */
public class DeviceInfo {
    private String name;
    private boolean isEnabled;
    private int clients;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getClients() {
        return clients;
    }

    public void setClients(int clients) {
        this.clients = clients;
    }
}
