package com.cuongnguyen.cse441.mapper;

import com.cuongnguyen.cse441.dto.request.OrderRequest;
import com.cuongnguyen.cse441.dto.request.UpdateOrderRequest;
import com.cuongnguyen.cse441.entity.Order;
import com.cuongnguyen.cse441.dto.response.OrderResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "orderItems", source = "orderItems")
    Order toOrder(OrderRequest orderRequest);

    @Mapping(target = "orderItems", source = "orderItems")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderItems", source = "orderItems")
    void updateOrder(@MappingTarget Order order, UpdateOrderRequest request);


    List<OrderResponse> toOrderResponseList(List<Order> allOrders);
}