package com.bymdev.artem.ecommercedemo.response;

import java.util.List;

public record OrderResponse(Integer id, Double total_amount, List<OrderItemResponse> orderItems) {
}
