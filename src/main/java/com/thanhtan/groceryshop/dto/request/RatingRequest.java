package com.thanhtan.groceryshop.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingRequest implements Serializable {
    @DecimalMin(message = "Rating start should be min 0.5", value = "0.5")
    @Max(message = "Rating start should be max 5", value = 5)
    double rate;
    Long productId;
    Long userId;
}