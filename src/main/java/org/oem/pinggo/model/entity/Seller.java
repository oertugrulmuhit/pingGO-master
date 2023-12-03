package org.oem.pinggo.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Seller {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String businessAddress;

    private String businessName;

    @OneToMany(cascade = {jakarta.persistence.CascadeType.ALL}, mappedBy = "seller")
    private Set<Product> products = new LinkedHashSet<>();

    public Seller(User user, String businessAddress, String businessName) {
        this.user = user;
        this.businessAddress = businessAddress;
        this.businessName = businessName;
    }

    public Seller(User currentUser) {
        this.user=currentUser;
    }
}