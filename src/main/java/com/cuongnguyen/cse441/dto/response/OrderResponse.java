package com.cuongnguyen.cse441.dto.response;

import com.cuongnguyen.cse441.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse extends BaseDTO implements Serializable {
    String customerName;
    String email;
    String phoneNumber;
    String address;
    Double totalPay;
    String note;
    OrderStatus orderStatus;

    CouponResponse coupon;

    @NotNull
    List<OrderItemResponse> orderItems;
}