package com.bymdev.artem.ecommercedemo.response;

import java.sql.Timestamp;
import java.util.List;

public record OrderResponse(Integer id, Double total_amount, Timestamp created_at, List<OrderItemResponse> orderItems) {
}
