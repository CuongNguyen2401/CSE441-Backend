package com.cuongnguyen.cse441.controller;

import com.cuongnguyen.cse441.dto.request.OrderRequest;
import com.cuongnguyen.cse441.dto.request.UpdateOrderRequest;
import com.cuongnguyen.cse441.dto.response.ApiResponse;
import com.cuongnguyen.cse441.dto.response.MonthlySalesResponse;
import com.cuongnguyen.cse441.dto.response.OrderResponse;
import com.cuongnguyen.cse441.enums.OrderStatus;
import com.cuongnguyen.cse441.service.IOrderService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cuongnguyen.cse441.constant.PathConstant.API_V1_ORDERS;

@RestController
@RequestMapping(API_V1_ORDERS)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    IOrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest) throws MessagingException {
        return ApiResponse.success(orderService.createOrder(orderRequest));
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<Page<OrderResponse>> getAllOrder(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String searchTerm) {
        return ApiResponse.success(orderService.getAllOrderWithoutOrderItems(pageable, searchTerm));
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrder(@PathVariable Long orderId) {
        return ApiResponse.success(orderService.getOrder(orderId));
    }

    @PutMapping()
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<OrderResponse> updateOrder(@RequestBody @Valid UpdateOrderRequest updateOrderRequest) {
        return ApiResponse.success(orderService.updateOrder(updateOrderRequest));
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<OrderResponse> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        return ApiResponse.success(orderService.updateOrderStatus(orderId, status));
    }

    @GetMapping("/monthly-sales")
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<List<MonthlySalesResponse>> getMonthlySales() {
        return ApiResponse.success(orderService.getMonthlySales());
    }

    @GetMapping("/monthly-sales-revenue")
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<Double> getMonthlySalesRevenue() {
        return ApiResponse.success(orderService.getMonthlyTotalSalesRevenue());
    }

    @GetMapping("/yearly-sales-revenue")
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<Double> getYearSalesRevenue() {
        return ApiResponse.success(orderService.getYearTotalSalesRevenue());
    }

    @GetMapping("/daily-sales")
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<Long> findNumberOfOrderDaily() {
        return ApiResponse.success(orderService.findNumberOfOrderDaily());
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<OrderResponse>> findAllOrdersByUser() {
        return ApiResponse.success(orderService.findAllOrdersByUser());
    }

}
