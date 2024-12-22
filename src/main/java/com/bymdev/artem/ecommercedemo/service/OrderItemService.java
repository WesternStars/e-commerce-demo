package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Order;
import com.bymdev.artem.ecommercedemo.entity.OrderItem;
import com.bymdev.artem.ecommercedemo.entity.Product;
import com.bymdev.artem.ecommercedemo.repository.OrderItemRepository;
import com.bymdev.artem.ecommercedemo.repository.ProductRepository;
import com.bymdev.artem.ecommercedemo.request.OrderItemRequest;
import com.bymdev.artem.ecommercedemo.response.OrderItemResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public static OrderItemResponse getOrderItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getQuantity(),
                ProductService.getProductResponse(item.getProduct()),
                Optional.ofNullable(item.getOrder()).map(Order::getId).orElse(0)
        );
    }

    public OrderItemResponse getOrderItem(int id) {
        return getOrderItemResponse(orderItemRepository.findById(id).orElseThrow());
    }

    public List<OrderItemResponse> getOrderItems(int count, int page) {
        return orderItemRepository.findAll(PageRequest.of(page, count))
                .stream()
                .map(OrderItemService::getOrderItemResponse)
                .toList();
    }

    public OrderItemResponse createOrderItem(OrderItemRequest request) {
        return saveOrderItem(mapToOrderItem(request));
    }

    public OrderItemResponse updateOrderItem(int id, OrderItemRequest request) {
        return saveOrderItem(mapToOrderItem(request, id));
    }

    public void delete(int id) {
        orderItemRepository.deleteById(id);
    }

    private OrderItem mapToOrderItem(OrderItemRequest request) {
        return mapToOrderItem(request, null);
    }

    private OrderItem mapToOrderItem(OrderItemRequest request, Integer id) {
        Product product = productRepository.findById(request.productSku()).orElseThrow();
        return new OrderItem(id, request.quantity(), product, null);
    }

    private OrderItemResponse saveOrderItem(OrderItem item) {
        OrderItem savedItem = orderItemRepository.save(item);
        return getOrderItemResponse(savedItem);
    }
}
