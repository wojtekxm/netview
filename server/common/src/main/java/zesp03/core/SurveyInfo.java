package zesp03.core;

//TODO zmień tą klasę w App i SNMPHandler
public class SurveyInfo {
    private String name;
    private boolean enabled;
    private int clientsSum;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
        return "{name=\"" + name + "\", enabled=" + enabled + ", clientsSum=" + clientsSum + ", id=" + id + "}";
    }
}
