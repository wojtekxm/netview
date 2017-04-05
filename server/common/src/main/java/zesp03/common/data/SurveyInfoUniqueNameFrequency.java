package zesp03.common.data;

public class SurveyInfoUniqueNameFrequency {
    private String name = "";
    private int frequencyMhz;
    private boolean enabled;
    private int clientsSum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null) {
            throw new IllegalArgumentException("name null");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyInfoUniqueNameFrequency that = (SurveyInfoUniqueNameFrequency) o;

        if (frequencyMhz != that.frequencyMhz) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + frequencyMhz;
        return result;
    }
}
