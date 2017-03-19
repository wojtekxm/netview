package zesp03.common.data;

//TODO zmień tą klasę w App i NetworkService
public class SurveyInfo {
    private String name;
    private boolean enabled;
    private int clientsSum;

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
}
