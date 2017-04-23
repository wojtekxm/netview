package zesp03.common.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "controller")
public class Controller {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "controller")
    @TableGenerator(name = "controller", pkColumnValue = "controller")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = true)
    private Building building;

    @Column(name = "\"name\"", nullable = false, length = 85)
    private String name;

    @Column(nullable = false, length = 15)
    private String ipv4;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(name = "community_string", nullable = false)
    private String communityString;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "controller", fetch = FetchType.LAZY)
    private List<Device> deviceList = new ArrayList<>();

    public Controller() {
        this.deleted = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Building getBuilding(){return building; }

    public void setBuilding(Building building) {this.building = building;}

    public String getCommunityString() {
        return communityString;
    }

    public void setCommunityString(String communityString) {
        this.communityString = communityString;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }
}
