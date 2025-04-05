package com.thanhtan.groceryshop.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponRequest {

    @Min(0)
    double discount;

    @Future
    @NotNull
    Date expiryDate;

    Long quantity;

    String description;

    List<Long> userIds;
}
