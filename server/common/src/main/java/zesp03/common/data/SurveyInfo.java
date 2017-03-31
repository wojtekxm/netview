package zesp03.common.data;

public class SurveyInfo {
    private String name;
    private int frequencyMhz;
    private boolean enabled;
    private int clientsSum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrequencyMhz() {
        return frequencyMhz;
    }

    public void setFrequencyMhz(int frequencyMhz) {
        this.frequencyMhz = frequencyMhz;
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
