package com.example.consumer.consumer;

import com.example.consumer.domain.coupon.Coupon;
import com.example.consumer.domain.coupon.repository.CouponRepository;

import com.example.consumer.domain.failed_event.FailedEvent;
import com.example.consumer.domain.failed_event.repository.FailedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;
    private final FailedEventRepository failedEventRepository;

    /**
     * [만약 여기서 에러가 터지면]
     * - Producer는 성공했다고 생각하겠지만
     * - 사실 Consumer에서는 에외가 터져 쿠폰 발급이 안되었고
     * - Producer는 100개 발급 성공! 하겠지만 -> 실제 DB를 까보면 그렇지 않을 수 있다.
     * - 그래서 insert에 실패시 -> 로그를 남기고 , 쿠폰발급에 성공했어야 하나 실패한 userId의 정보도 함께 쌓자
     * */
    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    @Transactional
    public void listener(Long userId) {

        try {

            couponRepository.save(Coupon.create(userId));
        } catch (Exception e) {

            log.error("Coupon Create Fail! Failed User Id : {} , error message : {}", userId, e.getCause().getMessage());
            failedEventRepository.save(FailedEvent.create(userId));
        }
    }
}
