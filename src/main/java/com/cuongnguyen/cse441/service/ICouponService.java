package com.cuongnguyen.cse441.service;

import com.cuongnguyen.cse441.dto.request.CouponRequest;
import com.cuongnguyen.cse441.dto.response.CouponResponse;

import java.util.List;

public interface ICouponService {
    List<CouponResponse> getAllCoupons();

    CouponResponse getCouponById(Long id);

    CouponResponse createGlobalCoupon(CouponRequest couponRequest);

    void deleteCouponByIds(Long [] ids);

    CouponResponse getCouponByCode(String code);
}
