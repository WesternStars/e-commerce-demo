package com.bymdev.artem.ecommercedemo.request;

import jakarta.validation.constraints.NotEmpty;

public record CatalogRequest(@NotEmpty(message = "The name is required.") String name) {
}
