package com.thanhtan.groceryshop.dto.request;

import com.thanhtan.groceryshop.enums.ProductStatus;
import com.thanhtan.groceryshop.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

    String name;
    String description;
    double price;
    double salePrice;
    int quantity;
    Long categoryId;
    ProductStatus productStatus;
    Set<Long> relatedProducts;

}