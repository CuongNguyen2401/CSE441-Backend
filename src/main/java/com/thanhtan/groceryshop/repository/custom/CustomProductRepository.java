package com.thanhtan.groceryshop.repository.custom;

import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CustomProductRepository {
    List<Tuple> findRevenueByCategoryInPeriod(Date startDate, Date endDate);
}
