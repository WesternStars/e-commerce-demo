package com.bymdev.artem.ecommercedemo.response;

public record ProductResponse(String sku, String name, Double price, CategoryResponse category) {
}
