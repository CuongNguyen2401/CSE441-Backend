package com.thanhtan.groceryshop.mapper;

import com.thanhtan.groceryshop.dto.response.OrderItemResponse;
import com.thanhtan.groceryshop.entity.OrderItem;
import com.thanhtan.groceryshop.dto.request.OrderItemRequest;
import com.thanhtan.groceryshop.entity.Product;
import com.thanhtan.groceryshop.repository.ProductRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapper {


    @Mapping(target = "product", source = "productId", ignore = true)
    OrderItem toOrderItem(OrderItemRequest orderItemRequest);


    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "image", source = "product.image")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);




}