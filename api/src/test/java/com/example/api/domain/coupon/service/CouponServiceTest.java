package com.example.api.domain.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.api.domain.coupon.repository.CouponRepository;
import com.example.api.global.enums.EntityStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback(value = false)
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @AfterEach
    public void afterEach() {

        couponRepository.deleteAll();
    }
    @Test
    @DisplayName("0개의 쿠폰이 발행되어 있을 때, 한명만 응모하여 쿠폰 발생에 성공")
    void 한명만응모하여_쿠폰발행성공() throws Exception {

        //given

        //when
        couponService.applyCoupon(1L);

        //then
        Long couponCount = couponRepository.countByEntityStatus(EntityStatus.ACTIVE);
        assertThat(couponCount).isEqualTo(1L);
    }
}