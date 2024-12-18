package com.bymdev.artem.ecommercedemo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
        @NotNull(message = "The quantity is required.")
        @Positive(message = "The quantity must be greater than 0")
        Integer quantity,
        @NotEmpty(message = "The productSku is required.")
        String productSku
) {
}
