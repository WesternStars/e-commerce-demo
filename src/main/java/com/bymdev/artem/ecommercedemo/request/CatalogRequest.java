package com.bymdev.artem.ecommercedemo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Catalog")
public record CatalogRequest(@Schema(description = "Name", example = "Tool") @NotEmpty(message = "The name is required.") String name) {
}
