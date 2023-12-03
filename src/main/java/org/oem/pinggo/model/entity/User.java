package org.oem.pinggo.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email")})
public class User extends BaseEntity {

    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean enabled = false;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ToString.Include
    private String name;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private VerificationToken verificationToken = new VerificationToken(this);

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Seller selleraccount;

    @OneToMany(mappedBy = "customer")
    private Set<Order> orders;

    public User(String username, String name, String email, String encode) {
        this.username = username;
        this.email = email;
        this.password = encode;
        this.name = name;

    }

//    @PrePersist
//    @Override
//    public void prePersist() {
//        super.prePersist();
//        this.verificationToken.setUser(this);
//    }
}