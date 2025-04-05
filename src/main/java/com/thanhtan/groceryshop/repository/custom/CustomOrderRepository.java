package com.thanhtan.groceryshop.repository.custom;

import com.querydsl.core.Tuple;
import com.thanhtan.groceryshop.dto.response.OrderResponse;
import com.thanhtan.groceryshop.entity.Order;

import java.util.List;

public interface CustomOrderRepository {
    List<Tuple> getMonthlySales();
    List<Tuple> findMostSoldProduct();
    Long findNumberOfOrderDaily();
    List<Order> findAllOrdersByUsername(String username);

    List<Tuple> getMonthlyTotalSalesRevenue();

    List<Tuple> getYearlyotalSalesRevenue();
}
