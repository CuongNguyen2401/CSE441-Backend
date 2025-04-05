package com.thanhtan.groceryshop.service;

import com.thanhtan.groceryshop.dto.request.CouponRequest;
import com.thanhtan.groceryshop.dto.response.CouponResponse;
import com.thanhtan.groceryshop.entity.Coupon;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ICouponService {
    List<CouponResponse> getAllCoupons();

    CouponResponse getCouponById(Long id);

    CouponResponse createGlobalCoupon(CouponRequest couponRequest);

    void deleteCouponByIds(Long [] ids);

    CouponResponse getCouponByCode(String code);
}
