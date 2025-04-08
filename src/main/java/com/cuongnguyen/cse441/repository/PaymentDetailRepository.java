package com.cuongnguyen.cse441.repository;

import com.cuongnguyen.cse441.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
}
