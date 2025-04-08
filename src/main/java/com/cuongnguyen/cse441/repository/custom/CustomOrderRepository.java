package com.cuongnguyen.cse441.repository.custom;

import com.querydsl.core.Tuple;
import com.cuongnguyen.cse441.entity.Order;

import java.util.List;

public interface CustomOrderRepository {
    List<Tuple> getMonthlySales();
    List<Tuple> findMostSoldProduct();
    Long findNumberOfOrderDaily();
    List<Order> findAllOrdersByUsername(String username);

    List<Tuple> getMonthlyTotalSalesRevenue();

    List<Tuple> getYearlyotalSalesRevenue();
}
