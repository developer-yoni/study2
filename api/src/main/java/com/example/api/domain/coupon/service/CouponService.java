package com.example.api.domain.coupon.service;

import com.example.api.domain.coupon.Coupon;
import com.example.api.domain.coupon.repository.AppliedUserRepository;
import com.example.api.domain.coupon.repository.CouponCountRepository;
import com.example.api.domain.coupon.repository.CouponRepository;
import com.example.api.global.enums.EntityStatus;
import com.example.api.global.kafka.producer.CouponCreateProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;

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

    @Transactional
    public synchronized void applyCouponSynchronized(Long userId) {

        /**
         * [레이스 컨디션 발생]
         * insert가 커밋 되기 직전 -> 먼저 lock이 해제되고 -> lock을 다시 잡은 쓰레드는 insert 되기 전 count 값을 읽어올 수 있으므로 -> 레이스 컨디션 문제 유지
         */
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

    @Transactional
    public void applyCouponWithRedisIncr(Long userId) {

        /**
         * redis의 incr로 인해 -> 싱글쓰레드로 레이스컨디션 없이 증가시킨 후 -> 그 증가된 값을 확인하고 save에 들어가니 -> 문제 해결
         */
        //1. redis incr로 레이스컨디션 문제 없이 먼저 1 증가 후 -> 증가된 값으로 비교
        Long incrementCouponCount = couponCountRepository.increment();

        //2_1. 100개 이상이면 발급 x
        if (incrementCouponCount >= MAXIMUM_COUPON_COUNT + 1) {

            return;
        }

        System.out.println("*******************");
        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / 읽어온 coupon 개수 : " + (incrementCouponCount -1));
        System.out.println("*******************");
        //2_2. 100개 미만이면 발급
        Coupon coupon = Coupon.create(userId);
        couponRepository.save(coupon);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void applyCouponForRequiresNew(Long userId) {

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

    @Transactional
    public void applyCouponWithKafka(Long userId) {

        /**
         * redis의 incr로 인해 -> 싱글쓰레드로 레이스컨디션 없이 증가시킨 후 -> 그 증가된 값을 확인하고 save에 들어가니 -> 문제 해결
         */
        //1. redis incr로 레이스컨디션 문제 없이 먼저 1 증가 후 -> 증가된 값으로 비교
        Long incrementCouponCount = couponCountRepository.increment();

        //2_1. 100개 이상이면 발급 x
        if (incrementCouponCount >= MAXIMUM_COUPON_COUNT + 1) {

            return;
        }

        System.out.println("*******************");
        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / 읽어온 coupon 개수 : " + incrementCouponCount);
        System.out.println("*******************");
        //2_2. 100개 미만이면 발급
        couponCreateProducer.create(userId);
    }

    @Transactional
    public void applyCouponWithKafkaAndRedisSet(Long userId) {

        /**
         * 이미 쿠폰을 발급받은 회원이라면 out -> 즉 User별로 1인당 1개만 발급됨
         * */
        if (appliedUserRepository.add(userId) != 1L) {

            return;
        }

        //1. redis incr로 레이스컨디션 문제 없이 먼저 1 증가 후 -> 증가된 값으로 비교
        Long incrementCouponCount = couponCountRepository.increment();

        //2_1. 100개 이상이면 발급 x
        if (incrementCouponCount >= MAXIMUM_COUPON_COUNT + 1) {

            return;
        }

        System.out.println("*******************");
        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / 읽어온 coupon 개수 : " + incrementCouponCount);
        System.out.println("*******************");
        //2_2. 100개 미만이면 발급
        couponCreateProducer.create(userId);
    }
}
