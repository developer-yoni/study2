package com.example.api.domain.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.api.domain.coupon.repository.CouponRepository;
import com.example.api.global.enums.EntityStatus;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    @Test
    @DisplayName("100개보다 더 많은 1000개의 요청이 동시에 왔을때, 한정된 100개의 쿠폰만 생성되는것에 실패")
    void _100개동시요청에대해_100개의한정수량_쿠폰생성에_실패() throws Exception {

        //given
        // ExecutorService는 멀티쓰레드 동장을 간단하게 도와주는 Java 모듈
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        //when
        for (int i = 0; i < threadCount; i++) {

            int finalI = i;
            executorService.submit(() -> {

                /** main tread에 의해 비동기로 호출되는 각 worker thread가 couponService.applyCoupon()을 호출한 후, finally 문에서 countDown() 호출한다 */
                try {

                    couponService.applyCoupon(Long.valueOf(finalI));
                } finally {

                    countDownLatch.countDown();
                }
            });
        }
        /** 비동기로 각 worker thread에게 작업을 위임한 후, 여기로 내려온 main thread가 , countDownLatch의 countDown이 0이 될때까지, main thread가 대기한다 */
        countDownLatch.await();

        //then
        Long couponCount = couponRepository.countByEntityStatus(EntityStatus.ACTIVE);
        assertThat(couponCount).isEqualTo(100L);
    }
}