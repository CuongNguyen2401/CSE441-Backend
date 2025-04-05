package com.thanhtan.groceryshop.service.impl;

import com.thanhtan.groceryshop.dto.request.CouponRequest;
import com.thanhtan.groceryshop.dto.request.NotificationRequest;
import com.thanhtan.groceryshop.dto.response.CouponResponse;
import com.thanhtan.groceryshop.entity.Coupon;
import com.thanhtan.groceryshop.entity.User;
import com.thanhtan.groceryshop.mapper.CouponMapper;
import com.thanhtan.groceryshop.repository.CouponRepository;
import com.thanhtan.groceryshop.repository.UserRepository;
import com.thanhtan.groceryshop.service.ICouponService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponService implements ICouponService {

    CouponRepository couponRepository;

    CouponMapper couponMapper;

    UserRepository userRepository;

    NotificationService notificationService;

    @Override
    public List<CouponResponse> getAllCoupons() {
        return couponMapper.toOrderResponseList(couponRepository.findAll());
    }

    @Override
    public CouponResponse getCouponById(Long id) {
        return couponMapper.toCouponResponse(couponRepository.getById(id));
    }

    @Override
    @Transactional
    public CouponResponse createGlobalCoupon(CouponRequest couponRequest) {
        Coupon coupon = couponMapper.toCoupon(couponRequest);
        coupon.setCode(UUID.randomUUID().toString());
        coupon.setGlobal(true);
        List<Long> userIds = couponRequest.getUserIds();

        if (userIds != null && !userIds.isEmpty()) {
            List<User> users = userRepository.findUsersByIds(userIds);
            users.forEach(user -> user.getCoupons().add(coupon));
            coupon.setUsers(new HashSet<>(users));
        }
        Coupon savedCoupon = couponRepository.save(coupon);
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTitle("New Coupon");
        notificationRequest.setContent("New coupon is available for you: " + coupon.getCode());

        notificationService.sendNotificationToUser(notificationRequest, userIds);

        return couponMapper.toCouponResponse(savedCoupon);
    }

    @Override
    @Transactional
    public void deleteCouponByIds(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        couponRepository.deleteUserCouponsByCouponIds(idList);
        couponRepository.deleteAllById(idList);
    }

    @Override
    public CouponResponse getCouponByCode(String code) {
        return couponMapper.toCouponResponse(couponRepository.findByCode(code));
    }
}
