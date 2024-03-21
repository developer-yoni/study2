package com.example.consumer.domain.failed_event;

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
@Table(name = "failed_event")
public class FailedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "failed_event")
    private Long id;

    @Column(name = "failed_user_id")
    private Long failedUserId; // coupon 발급에 장애가 나서, coupon을 발급해줘야 했지만 발급이 안된 User의 Id

    /**
     * [Create Static Factory Method]
     * */
    public static FailedEvent create(Long failedUserId) {

        return FailedEvent.builder()
                          .failedUserId(failedUserId)
                          .build();
    }
}
