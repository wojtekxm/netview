package zesp03.data;

public class DeviceState {
    private String name;
    private boolean isEnabled;
    private int clientsSum;
    private int id;

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

    public int getClientsSum() {
        return clientsSum;
    }

    public void setClientsSum(int clientsSum) {
        this.clientsSum = clientsSum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{name=\"" + name + "\", enabled=" + isEnabled + ", clientsSum=" + clientsSum + ", id=" + id + "}";
    }
}
