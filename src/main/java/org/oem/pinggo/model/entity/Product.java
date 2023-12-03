package org.oem.pinggo.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(message = "max 60 char", max = 60)
    @NotEmpty(message = "Please write anything for name")
    private String name;

    @NotEmpty(message = "Please write anything for desc.")
    @Column(name = "description", nullable = false)
    private String description;

    @JoinColumn(name = "seller_id")
    @ManyToOne
    private Seller seller;

    private int quantity;

    @OneToMany(mappedBy = "product")
    private Set<Order> orders;
}