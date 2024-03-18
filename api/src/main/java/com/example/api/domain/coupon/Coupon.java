package com.example.api.domain.coupon;

import com.example.api.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "coupon")
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    /**
     * [Foreign Key]
     * */
    // 발급받은 user_id
    @Column(name = "user_id")
    private Long userId;

    /**
     * [Create Static Factory Method]
     * */
    public static Coupon create(Long userId) {

        return Coupon.builder()
                     .userId(userId)
                     .build();
    }
}
