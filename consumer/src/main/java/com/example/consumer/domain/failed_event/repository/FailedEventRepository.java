package com.example.consumer.domain.failed_event.repository;

import com.example.consumer.domain.failed_event.FailedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedEventRepository extends JpaRepository<FailedEvent, Long> {

}
