package com.thanhtan.groceryshop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thanhtan.groceryshop.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ProductResponse extends BaseDTO {
    String name;
    String description;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    double price;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    double salePrice;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    int quantity;
    String image;
    String slug;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    double ratings;

    ProductStatus productStatus;

    CategoryResponse category;

    Set<ProductResponse> relatedProducts;
}
