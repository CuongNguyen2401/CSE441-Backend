package com.cuongnguyen.cse441.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSalesResponse {
    ProductResponse product;
    Long totalQuantitySold;
}
