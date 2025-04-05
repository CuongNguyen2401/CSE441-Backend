package com.thanhtan.groceryshop.service;

import com.thanhtan.groceryshop.dto.request.OrderItemRequest;
import com.thanhtan.groceryshop.dto.request.OrderRequest;
import com.thanhtan.groceryshop.dto.request.UpdateOrderRequest;
import com.thanhtan.groceryshop.dto.response.MonthlySalesResponse;
import com.thanhtan.groceryshop.dto.response.OrderResponse;
import com.thanhtan.groceryshop.dto.response.ProductSalesResponse;
import com.thanhtan.groceryshop.entity.Order;
import com.thanhtan.groceryshop.enums.OrderStatus;
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
