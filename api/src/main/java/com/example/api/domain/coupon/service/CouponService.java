package com.example.api.domain.coupon.service;

import com.example.api.domain.coupon.Coupon;
import com.example.api.domain.coupon.repository.CouponRepository;
import com.example.api.global.enums.EntityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final Long MAXIMUM_COUPON_COUNT = 100L;

    @Transactional
    public void applyCoupon(Long userId) {

        //1. coupone 개수 조회 후
        Long couponCount = couponRepository.countByEntityStatus(EntityStatus.ACTIVE);

        //2_1. 100개 이상이면 발급 x
        if (couponCount >= MAXIMUM_COUPON_COUNT) {

            return;
        }

        //2_2. 100개 미만이면 발급
        Coupon coupon = Coupon.create(userId);
        couponRepository.save(coupon);
    }
}
