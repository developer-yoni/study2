package com.example.api.domain.coupon.repository;

import com.example.api.domain.coupon.Coupon;
import com.example.api.global.enums.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Long countByEntityStatus(EntityStatus entityStatus);
}
