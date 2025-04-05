package com.thanhtan.groceryshop.repository;

import com.thanhtan.groceryshop.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
}
