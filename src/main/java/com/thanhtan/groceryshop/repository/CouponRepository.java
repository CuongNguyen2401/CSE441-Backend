package com.thanhtan.groceryshop.repository;

import com.thanhtan.groceryshop.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findByCode(String code);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_coupons WHERE coupon_id IN ?1", nativeQuery = true)
    void deleteUserCouponsByCouponIds(List<Long> couponIds);
}