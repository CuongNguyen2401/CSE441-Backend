package com.thanhtan.groceryshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.thanhtan.groceryshop.enums.ProductStatus;
import jakarta.persistence.*;
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
public class Product extends BaseEntity {

    String name;
    String description;
    double price;
    double salePrice;
    int quantity;
    String image;
    String slug;

    @Enumerated(EnumType.STRING)
    ProductStatus productStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<OrderItem> orderItems = new HashSet<>();


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Rating> ratings = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "related_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "related_product_id")
    )
    @JsonIgnore
    Set<Product> relatedProducts = new HashSet<>();

    @Version
    private long version;

    @PostPersist
    public void createInitialRating() {
        if (this.ratings == null) {
            this.ratings = new HashSet<>();
        }

        Rating rating = new Rating();
        rating.setUserId(0L);
        rating.setRate(0.0);
        rating.setProduct(this);
        this.ratings.add(rating);
    }
}
