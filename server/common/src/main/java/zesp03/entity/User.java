package zesp03.entity;

import javax.persistence.*;

@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user")
    @TableGenerator(name = "user", pkColumnValue = "user")
    private Long id;

    @Column(name = "\"name\"", unique = true)
    private String name;

    @Column(length = 132)
    private byte[] secret;

    @Column(name = "is_activated", nullable = false)
    private Boolean activated;

    @Column(name = "is_blocked", nullable = false)
    private Boolean blocked;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('NORMAL', 'ADMIN', 'ROOT')")
    private UserRole role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getSecret() {
        return secret;
    }

    public void setSecret(byte[] secret) {
        this.secret = secret;
    }

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
