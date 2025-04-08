package com.cuongnguyen.cse441.mapper;

import com.cuongnguyen.cse441.dto.request.CouponRequest;
import com.cuongnguyen.cse441.dto.response.CouponResponse;
import com.cuongnguyen.cse441.entity.Coupon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponMapper {


    @Mapping(target ="users", source = "userIds", ignore = true)
    @Mapping(target = "description", source = "description")
    Coupon toCoupon(CouponRequest couponRequest);


    CouponResponse toCouponResponse(Coupon coupon);

    List<CouponResponse> toOrderResponseList(List<Coupon> allCoupons);

}
