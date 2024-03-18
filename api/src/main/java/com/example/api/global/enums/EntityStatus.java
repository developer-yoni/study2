package com.example.api.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntityStatus {

    ACTIVE("엔티티가 Soft Delete 되지 않은 상태"),
    INACTIVE("엔티티가 Soft Delete 된 상태");
    private final String description;
}
