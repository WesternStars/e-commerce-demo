package com.bymdev.artem.ecommercedemo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @Schema(example = "ABC0001")
        @NotEmpty(message = "The sku is required.")
        String sku,
        @Schema(example = "Hammer")
        @NotEmpty(message = "The name is required.")
        String name,
        @Schema(example = "12.5")
        @NotNull(message = "The price is required.")
        @Positive(message = "The price must be greater than 0")
        Double price,
        @Schema(example = "1")
        @NotNull(message = "The categoryId is required.")
        @Positive(message = "The categoryId must be greater than 0")
        Integer categoryId
) {
}
