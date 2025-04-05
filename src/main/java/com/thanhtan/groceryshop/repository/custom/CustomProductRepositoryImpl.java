package com.thanhtan.groceryshop.repository.custom;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thanhtan.groceryshop.entity.QCategory;
import com.thanhtan.groceryshop.entity.QOrder;
import com.thanhtan.groceryshop.entity.QOrderItem;
import com.thanhtan.groceryshop.entity.QProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class CustomProductRepositoryImpl implements CustomProductRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<Tuple> findRevenueByCategoryInPeriod(Date startDate, Date endDate) {
        QOrder order = QOrder.order;
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;
        QOrderItem orderItem = QOrderItem.orderItem;

        return queryFactory
                .select(category.name, order.totalPay.sum())
                .from(order)
                .join(order.orderItems, orderItem)
                .join(orderItem.product, product)
                .join(product.category, category)
                .where(order.createdDate.between(startDate, endDate))
                .groupBy(category.name)
                .fetch();

    }
}
