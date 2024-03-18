package com.example.api.domain.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.api.domain.coupon.facade.CouponFacade;
import com.example.api.domain.coupon.repository.CouponCountRepository;
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

    @Autowired
    private CouponCountRepository couponCountRepository;

    @Autowired
    private CouponFacade couponFacade;

    @AfterEach
    public void afterEach() {

        couponRepository.deleteAll();
        couponCountRepository.reset();
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

        long startTime = System.currentTimeMillis();
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
        long endTime = System.currentTimeMillis();

        //then
        Long couponCount = couponRepository.countByEntityStatus(EntityStatus.ACTIVE);
        System.out.println("[수행 시간] : " + (endTime - startTime) + " ms");
        assertThat(couponCount).isEqualTo(100L);
    }

    @Test
    @DisplayName("synchronized키워드를 쓰면 -> insert commit 전 lock이 해제되므로, 래이스컨디션 문제 유지")
    void syncrhonized키워드는_레이스컨디션문제를_해결할수없음() throws Exception {

        //given
        // ExecutorService는 멀티쓰레드 동장을 간단하게 도와주는 Java 모듈
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis();
        //when
        for (int i = 0; i < threadCount; i++) {

            int finalI = i;
            executorService.submit(() -> {

                /** main tread에 의해 비동기로 호출되는 각 worker thread가 couponService.applyCoupon()을 호출한 후, finally 문에서 countDown() 호출한다 */
                try {

                    couponService.applyCouponSynchronized(Long.valueOf(finalI));
                } finally {

                    countDownLatch.countDown();
                }
            });
        }
        /** 비동기로 각 worker thread에게 작업을 위임한 후, 여기로 내려온 main thread가 , countDownLatch의 countDown이 0이 될때까지, main thread가 대기한다 */
        countDownLatch.await();
        long endTime = System.currentTimeMillis();

        //then
        Long couponCount = couponRepository.countByEntityStatus(EntityStatus.ACTIVE);
        System.out.println("[수행 시간] : " + (endTime - startTime) + " ms");
        assertThat(couponCount).isEqualTo(100L);
    }

    @Test
    @DisplayName("Redis incr로 레이스컨디션 없이 1씩 증가시키면, 레이스컨디션 문제 해결")
    void red간is_incr명령어로_couponCount를_증가시켜_레이스컨디션문제해결() throws Exception {

        //given
        // ExecutorService는 멀티쓰레드 동장을 간단하게 도와주는 Java 모듈
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis();
        //when
        for (int i = 0; i < threadCount; i++) {

            int finalI = i;
            executorService.submit(() -> {

                /** main tread에 의해 비동기로 호출되는 각 worker thread가 couponService.applyCoupon()을 호출한 후, finally 문에서 countDown() 호출한다 */
                try {

                    couponService.applyCouponWithRedisIncr(Long.valueOf(finalI));
                } finally {

                    countDownLatch.countDown();
                }
            });
        }
        /** 비동기로 각 worker thread에게 작업을 위임한 후, 여기로 내려온 main thread가 , countDownLatch의 countDown이 0이 될때까지, main thread가 대기한다 */
        countDownLatch.await();
        long endTime = System.currentTimeMillis();

        //then
        Long couponCount = couponRepository.countByEntityStatus(EntityStatus.ACTIVE);
        System.out.println("[수행 시간] : " + (endTime - startTime) + " ms");
        assertThat(couponCount).isEqualTo(100L);
    }

    @Test
    @DisplayName("NamedLock으로 countBy부터 insert까지를 critical section으로 만든다면, 성능이 얼마나 나올지")
    void NamedLock으로_critical_section_구현후_couponCount를_증가시켜_레이스컨디션문제해결() throws Exception {

        //given
        // ExecutorService는 멀티쓰레드 동장을 간단하게 도와주는 Java 모듈
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis();
        //when
        for (int i = 0; i < threadCount; i++) {

            int finalI = i;
            executorService.submit(() -> {

                /** main tread에 의해 비동기로 호출되는 각 worker thread가 couponService.applyCoupon()을 호출한 후, finally 문에서 countDown() 호출한다 */
                try {

                    couponFacade.applyCouponWithNamedLock(Long.valueOf(finalI));
                } finally {

                    countDownLatch.countDown();
                }
            });
        }
        /** 비동기로 각 worker thread에게 작업을 위임한 후, 여기로 내려온 main thread가 , countDownLatch의 countDown이 0이 될때까지, main thread가 대기한다 */
        countDownLatch.await();
        long endTime = System.currentTimeMillis();

        //then
        Long couponCount = couponRepository.countByEntityStatus(EntityStatus.ACTIVE);
        System.out.println("[수행 시간] : " + (endTime - startTime) + " ms");
        assertThat(couponCount).isEqualTo(100L);
    }
}