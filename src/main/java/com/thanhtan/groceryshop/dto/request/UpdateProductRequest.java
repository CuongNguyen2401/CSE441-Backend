package com.thanhtan.groceryshop.dto.request;

import com.thanhtan.groceryshop.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductRequest {
    Long id;
    String name;
    String description;
    double price;
    double salePrice;
    int quantity;
    Long categoryId;
    ProductStatus productStatus;
    Set<Long> relatedProducts;
}
