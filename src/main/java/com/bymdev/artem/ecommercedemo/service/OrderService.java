package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Order;
import com.bymdev.artem.ecommercedemo.entity.OrderItem;
import com.bymdev.artem.ecommercedemo.repository.OrderItemRepository;
import com.bymdev.artem.ecommercedemo.repository.OrderRepository;
import com.bymdev.artem.ecommercedemo.request.OrderRequest;
import com.bymdev.artem.ecommercedemo.response.OrderResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderResponse getOrder(int id) {
        Order order = orderRepository.findById(id).orElseThrow();
        return mapToResponse(order, order.getOrderItems());
    }

    public List<OrderResponse> getOrders(int count, int page) {
        return orderRepository.findAll(PageRequest.of(page, count))
                .stream()
                .map(order -> mapToResponse(order, order.getOrderItems()))
                .toList();
    }

    public OrderResponse createOrder(OrderRequest request) {
        return saveOrder(request);
    }

    public OrderResponse updateOrder(int id, OrderRequest request) {
        orderRepository.findById(id).orElseThrow();
        return saveOrder(request, id);
    }

    public void delete(int id) {
        if (!orderItemRepository.findAllByOrder_Id(id).isEmpty()) {
            throw new RuntimeException("You  should delete all dependencies of this order.");
        }
        orderRepository.deleteById(id);
    }

    private OrderResponse saveOrder(OrderRequest request) {
        return saveOrder(request, null);
    }

    private OrderResponse saveOrder(OrderRequest request, Integer id) {
        Order requestOrder = mapToOrder(id, request);
        Order saved = orderRepository.save(requestOrder);
        List<OrderItem> orderItems = requestOrder.getOrderItems();
        List<OrderItem> assignedItems = orderItems.stream().peek(item -> item.setOrder(saved)).toList();
        orderItemRepository.saveAll(assignedItems);
        return mapToResponse(saved, assignedItems);
    }

    private Order mapToOrder(Integer id, OrderRequest request) {
        List<OrderItem> items = getOrderItems(request.orderItemIds());
        return new Order(id, calculateTotalAmount(items), items, new Timestamp(System.currentTimeMillis()));
    }

    private List<OrderItem> getOrderItems(List<Integer> orderItemIds) {
        List<OrderItem> items = new ArrayList<>();
        orderItemRepository.findAllById(orderItemIds).forEach(items::add);
        if (items.size() < orderItemIds.size()) {
            throw new NoSuchElementException("Could not find some items. Please check the orderItemIds.");
        }
        return items;
    }

    private OrderResponse mapToResponse(Order order, List<OrderItem> items) {
        return new OrderResponse(
                order.getId(),
                order.getTotal_amount(),
                order.getCreatedAt(),
                items.stream().map(OrderItemService::mapToResponse).toList()
        );
    }

    private Double calculateTotalAmount(List<OrderItem> items) {
        return items.stream()
                .map(i -> i.getQuantity() * i.getProduct().getPrice())
                .reduce(0.0, Double::sum);
    }
}
