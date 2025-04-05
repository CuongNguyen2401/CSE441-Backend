package com.thanhtan.groceryshop.mapper;

import com.thanhtan.groceryshop.dto.request.ProductRequest;
import com.thanhtan.groceryshop.dto.request.UpdateProductRequest;
import com.thanhtan.groceryshop.dto.response.ProductResponse;
import com.thanhtan.groceryshop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "relatedProducts", ignore = true)
    Product toProduct(ProductRequest request);

    @Mapping(target = "relatedProducts", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    ProductResponse toProductResponse(Product product);


    @Mapping(target = "relatedProducts", ignore = true)
    void updateProduct(@MappingTarget Product product, UpdateProductRequest request);


}
