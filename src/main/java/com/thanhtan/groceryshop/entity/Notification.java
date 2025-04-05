package com.thanhtan.groceryshop.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Notification extends BaseEntity {

    String title;
    String content;
    String linkUrl;
    boolean seen;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    User user;


}
