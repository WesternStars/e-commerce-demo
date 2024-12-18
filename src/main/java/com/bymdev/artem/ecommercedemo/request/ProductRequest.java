package com.bymdev.artem.ecommercedemo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @NotEmpty(message = "The sku is required.")
        String sku,
        @NotEmpty(message = "The name is required.")
        String name,
        @NotNull(message = "The price is required.")
        @Positive(message = "The price must be greater than 0")
        Double price,
        @NotNull(message = "The categoryId is required.")
        @Positive(message = "The categoryId must be greater than 0")
        Integer categoryId
) {
}
