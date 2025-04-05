package com.thanhtan.groceryshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDetail extends BaseEntity {

    String totalPrice;

    String provider;

    String paymentTime;

    String transactionId;

    String orderInfo;

    @OneToOne(mappedBy = "paymentDetail")
    Order order;
}
