package com.bymdev.artem.ecommercedemo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCatalogRequest(
        @NotNull(message = "The price is required.")
        @Positive(message = "The price must be greater than 0")
        Integer id,
        @NotEmpty(message = "The name is required.")
        String name
) {
}
