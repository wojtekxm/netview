package zesp03.entity;

import javax.persistence.*;

@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "token")
    @TableGenerator(name = "token", pkColumnValue = "token")
    private Long id;

    @Column(length = 132, nullable = false)
    private byte[] secret;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('RESET_PASSWORD', 'ACTIVATE_ACCOUNT')")
    private TokenAction action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "token_user_fk"),
            nullable = false
    )
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getSecret() {
        return secret;
    }

    public void setSecret(byte[] secret) {
        this.secret = secret;
    }

    public TokenAction getAction() {
        return action;
    }

    public void setAction(TokenAction action) {
        this.action = action;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
