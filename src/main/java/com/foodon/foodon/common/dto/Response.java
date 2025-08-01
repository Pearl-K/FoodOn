package com.foodon.foodon.common.dto;

import lombok.Builder;

@Builder
public record Response<T> (
        String code,
        String message,
        T data
) {
}
