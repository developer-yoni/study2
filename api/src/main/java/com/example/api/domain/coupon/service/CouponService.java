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

        /**
         * [레이스 컨디션 발생]
         * coupon 개수를 읽어와서 -> insert 하기까지의 section이 critical section이 아니므로
         *                   -> 읽어온 coupon 개수가 아직 (insert 예정이지만) insert 되기 전 값을 읽어오면 문제 발생 */
        //1. coupone 개수 조회 후
        Long couponCount = couponRepository.countByEntityStatus(EntityStatus.ACTIVE);

        //2_1. 100개 이상이면 발급 x
        if (couponCount >= MAXIMUM_COUPON_COUNT) {

            return;
        }

        System.out.println("*******************");
        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / 읽어온 coupon 개수 : " + couponCount);
        System.out.println("*******************");
        //2_2. 100개 미만이면 발급
        Coupon coupon = Coupon.create(userId);
        couponRepository.save(coupon);
    }
}
