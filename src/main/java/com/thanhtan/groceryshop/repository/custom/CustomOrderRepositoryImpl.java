package com.thanhtan.groceryshop.repository.custom;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thanhtan.groceryshop.dto.response.OrderResponse;
import com.thanhtan.groceryshop.entity.Order;
import com.thanhtan.groceryshop.entity.QOrder;
import com.thanhtan.groceryshop.entity.QOrderItem;
import com.thanhtan.groceryshop.entity.QUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<Tuple> getMonthlySales() {
        QOrder order = QOrder.order;

        return queryFactory
                .select(Expressions.asNumber(
                        order.createdDate.month()).as("month"),
                        order.totalPay.sum().as("sales")
                )
                .from(order)
                .groupBy(order.createdDate.month())
                .fetch();
    }

    @Override
    public List<Tuple> findMostSoldProduct() {
        QOrderItem orderItem = QOrderItem.orderItem;
        QOrder order = QOrder.order;

        return queryFactory.select(orderItem.product, orderItem.quantity.sum())
                .from(order)
                .join(order.orderItems, orderItem)
                .groupBy(orderItem.product)
                .orderBy(orderItem.quantity.sum().desc())
                .fetch();
    }

    @Override
    public Long findNumberOfOrderDaily() {
        QOrder order = QOrder.order;
        LocalDate currentDate = LocalDate.now();
        Long orderCount = queryFactory.select(order.count())
                .from(order)
                .where(order.createdDate.isNotNull()
                        .and(order.createdDate.after(java.sql.Timestamp.valueOf(currentDate.atStartOfDay())))
                        .and(order.createdDate.before(java.sql.Timestamp.valueOf(currentDate.plusDays(1).atStartOfDay()))))
                .fetchOne();
        return orderCount != null ? orderCount : 0;
    }

    @Override
    public List<Tuple> getYearlyotalSalesRevenue() {
        QOrder order = QOrder.order;

        return queryFactory.select(Expressions.asNumber(
                order.createdDate.year()).as("year"),
                order.totalPay.sum().as("revenue")
        )
                .from(order)
                .groupBy(order.createdDate.year())
                .fetch();
    }

    @Override
    public List<Tuple> getMonthlyTotalSalesRevenue() {
        QOrder order = QOrder.order;

        return queryFactory.select(Expressions.asNumber(
                order.createdDate.month()).as("month"),
                order.totalPay.sum().as("revenue")
        )
                .from(order)
                .groupBy(order.createdDate.month())
                .fetch();

    }

    @Override
    public List<Order> findAllOrdersByUsername(String username) {
        QOrder order = QOrder.order;
        QUser user = QUser.user;

        return queryFactory.select(orderProjection())
                .from(order)
                .join(order.user, user)
                .where(user.username.eq(username))
                .orderBy(order.createdDate.desc())
                .fetch();
    }

    private QBean<Order> orderProjection() {
        QOrder qOrder = QOrder.order;
        return Projections.bean(Order.class,
                qOrder.id,
                qOrder.createdDate,
                qOrder.customerName,
                qOrder.email,
                qOrder.phoneNumber,
                qOrder.address,
                qOrder.totalPay,
                qOrder.note,
                qOrder.orderStatus);
    }
}
