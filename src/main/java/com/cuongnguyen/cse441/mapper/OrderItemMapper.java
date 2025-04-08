package com.cuongnguyen.cse441.mapper;

import com.cuongnguyen.cse441.dto.response.OrderItemResponse;
import com.cuongnguyen.cse441.entity.OrderItem;
import com.cuongnguyen.cse441.dto.request.OrderItemRequest;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapper {


    @Mapping(target = "product", source = "productId", ignore = true)
    OrderItem toOrderItem(OrderItemRequest orderItemRequest);


    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "image", source = "product.image")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);




}