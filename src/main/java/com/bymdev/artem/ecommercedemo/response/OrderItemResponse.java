package com.bymdev.artem.ecommercedemo.response;

public record OrderItemResponse(Integer id, Integer quantity, ProductResponse product, Integer orderId) {
}
