package com.thanhtan.groceryshop.entity;

import com.thanhtan.groceryshop.enums.Gender;
import com.thanhtan.groceryshop.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String phoneNumber;
    @Enumerated(EnumType.STRING)
    Gender gender;
    String address;
    String email;
    String avatar;
    @Enumerated(EnumType.STRING)
    Status status;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications;

    @ManyToMany
    Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Order> orders = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_coupons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    private Set<Coupon> coupons;
}