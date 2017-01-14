package zesp03.entity;

public class Device {
    private int id;
    private String name;
    private boolean isKnown;
    private String description;
    private int controllerId;

    public Device() {
        //?
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsKnown() {
        return isKnown;
    }

    public void setIsKnown(boolean known) {
        isKnown = known;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getControllerId() {
        return controllerId;
    }

    public void setControllerId(int controllerId) {
        this.controllerId = controllerId;
    }
}