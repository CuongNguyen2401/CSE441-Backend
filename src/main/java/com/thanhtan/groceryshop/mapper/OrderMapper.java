package com.thanhtan.groceryshop.mapper;

import com.thanhtan.groceryshop.dto.request.OrderRequest;
import com.thanhtan.groceryshop.dto.request.ProductRequest;
import com.thanhtan.groceryshop.dto.request.UpdateOrderRequest;
import com.thanhtan.groceryshop.entity.Order;
import com.thanhtan.groceryshop.dto.response.OrderResponse;
import com.thanhtan.groceryshop.entity.Product;
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