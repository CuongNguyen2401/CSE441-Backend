package com.cuongnguyen.cse441.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "coupons")
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String code;

    @Column(nullable = false)
    double discount;

    @Column(nullable = false)
    Date expiryDate;

    @Column(nullable = false)
    boolean isGlobal;

    String description;

    @Column(nullable = false)
    int quantity;

    @ManyToMany(mappedBy = "coupons", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<User> users;

    @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY)
    Set<Order> orders = new HashSet<>();

}
