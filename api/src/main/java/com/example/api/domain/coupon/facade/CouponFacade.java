package com.example.api.domain.coupon.facade;

import com.example.api.domain.coupon.repository.LockRepository;
import com.example.api.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CouponFacade {

    private final LockRepository lockRepository;
    private final CouponService couponService;

    @Transactional
    public void applyCouponWithNamedLock(Long userId) {

        try {

            //1. 락 획득
            lockRepository.getLock("coupon_count");

            //2. 재고 감소
            // 이떄 Lock 획득 트랜잭션과 재고 감소 트랜잭션을 분리해야 함
            // 그렇지 않으면 재고감소가 커밋되기 전에, Lock이 해제될 수 있고 -> 그러면 아직 감소되지 않은 값을 읽을 수 있음
            // 따라서 트랜잭션을 분리히하여 , 락을 해제할 땐 반으시 업데이트를 반영한 후 -> 라는 사실을 보장해야 함
            couponService.applyCouponForRequiresNew(userId);
        } finally {

            //3. 명시적으로 락 해제
            lockRepository.getLock("coupon_count");
        }
    }
}
