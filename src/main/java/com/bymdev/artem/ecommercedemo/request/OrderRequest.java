package com.bymdev.artem.ecommercedemo.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequest(@NotEmpty(message = "The orderItemIds is required.") List<Integer> orderItemIds) {
}
