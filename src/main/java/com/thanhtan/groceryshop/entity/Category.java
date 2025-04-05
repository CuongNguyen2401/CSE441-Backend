package com.thanhtan.groceryshop.entity;

import com.thanhtan.groceryshop.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Category extends  BaseEntity{
    String name;

    String description;

    @Enumerated(EnumType.STRING)
    Status status;

    @OneToMany(mappedBy="category")
    Set<Product> products = new HashSet<>();
}
