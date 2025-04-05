package com.thanhtan.groceryshop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponResponse {

    Long id;

    String code;

    double discount;

    LocalDateTime expiryDate;

    String description;

    long quantity;
}
