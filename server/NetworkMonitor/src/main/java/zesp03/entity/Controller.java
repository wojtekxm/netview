package zesp03.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "controller")
public class Controller {
    @Id
    private int id;

    @Basic(optional = false)
    private String name;

    @Basic(optional = false)
    private String ipv4;

    @Basic(optional = true)
    private String description;
}
