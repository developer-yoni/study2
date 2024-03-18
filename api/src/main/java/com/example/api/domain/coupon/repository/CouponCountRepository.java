package com.example.api.domain.coupon.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponCountRepository {

    // Redis 명령어를 실행시킬 수 있어야 하므로 -> RedisTemplate를 의존성 받기
    private final RedisTemplate<String, String> redisTemplate;

    public Long increment() {

        return redisTemplate
                .opsForValue()
                .increment("coupon_count");
    }

    public void reset() {

        redisTemplate.execute((RedisConnection connection) -> {

            connection.flushAll();
            return null;
        });
    }
}
