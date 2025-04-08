package com.cuongnguyen.cse441.mapper;

import com.cuongnguyen.cse441.dto.request.ProductRequest;
import com.cuongnguyen.cse441.dto.request.UpdateProductRequest;
import com.cuongnguyen.cse441.dto.response.ProductResponse;
import com.cuongnguyen.cse441.entity.Product;
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
