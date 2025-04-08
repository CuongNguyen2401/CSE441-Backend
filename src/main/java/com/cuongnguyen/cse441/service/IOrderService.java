package com.cuongnguyen.cse441.service;

import com.cuongnguyen.cse441.dto.request.OrderItemRequest;
import com.cuongnguyen.cse441.dto.request.OrderRequest;
import com.cuongnguyen.cse441.dto.request.UpdateOrderRequest;
import com.cuongnguyen.cse441.dto.response.MonthlySalesResponse;
import com.cuongnguyen.cse441.dto.response.OrderResponse;
import com.cuongnguyen.cse441.entity.Order;
import com.cuongnguyen.cse441.enums.OrderStatus;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderRequest orderRequest) throws MessagingException;

    OrderResponse updateOrder(UpdateOrderRequest orderRequest);

    OrderResponse updateOrderStatus(Long orderId, OrderStatus status);

    OrderResponse getOrder(Long orderId);

    List<OrderResponse> getAllOrderWithoutOrderItems();

    Page<OrderResponse> getAllOrderWithoutOrderItems(Pageable pageable,String searchTerm);

    List<MonthlySalesResponse> getMonthlySales();

    Long findNumberOfOrderDaily();

    List<OrderResponse> findAllOrdersByUser();

    Double getMonthlyTotalSalesRevenue();

    Double getYearTotalSalesRevenue();

    double processOrderItems(Order savedOrder, List<OrderItemRequest> orderItems);
}
