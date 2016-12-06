package zesp03.pojo;

public class SurveyItem {
    private int id;
    private final int clients;
    private final boolean enabled;

    public SurveyItem(int clients, boolean isEnabled) {
        this.id = -1;
        this.clients = clients;
        this.enabled = isEnabled;
    }

    public int getId() {
        return id;
    }

    public int getClients() {
        return clients;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setId(int id) {
        this.id = id;
    }
}
