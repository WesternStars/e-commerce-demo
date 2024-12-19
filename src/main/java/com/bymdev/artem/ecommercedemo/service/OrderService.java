package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Order;
import com.bymdev.artem.ecommercedemo.entity.OrderItem;
import com.bymdev.artem.ecommercedemo.repository.OrderItemRepository;
import com.bymdev.artem.ecommercedemo.repository.OrderRepository;
import com.bymdev.artem.ecommercedemo.request.OrderRequest;
import com.bymdev.artem.ecommercedemo.response.OrderItemResponse;
import com.bymdev.artem.ecommercedemo.response.OrderResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
        return new OrderResponse(
                order.getId(),
                order.getTotal_amount(),
                order.getOrderItems().stream().map(orderItem -> new OrderItemResponse(
                        orderItem.getId(),
                        orderItem.getQuantity(),
                        orderItem.getProduct().getSku(),
                        orderItem.getOrder().getId()
                )).toList());
    }

    public List<OrderResponse> getOrders(int count, int page) {
        return orderRepository.findAll(PageRequest.of(page, count))
                .stream()
                .map(order -> new OrderResponse(order.getId(),
                        order.getTotal_amount(),
                        order.getOrderItems().stream().map(orderItem -> new OrderItemResponse(
                                orderItem.getId(),
                                orderItem.getQuantity(),
                                orderItem.getProduct().getSku(),
                                orderItem.getOrder().getId()
                        )).toList()))
                .toList();
    }

    public OrderResponse createOrder(OrderRequest request) {
        return addOrUpdateOrder(0, request);
    }

    public OrderResponse updateOrder(int id, OrderRequest request) {
        return addOrUpdateOrder(id, request);
    }

    public void delete(int id) {
        if (!orderItemRepository.findAllByOrder_Id(id).isEmpty()) {
            throw new RuntimeException("You  should delete all dependencies of this order.");
        }
        orderRepository.deleteById(id);
    }

    private OrderResponse addOrUpdateOrder(int id, OrderRequest request) {
        Order order = id > 0 ? orderRepository.findById(id).orElseThrow() : new Order();
        List<OrderItem> items = new ArrayList<>();
        orderItemRepository.findAllById(request.orderItemIds()).forEach(items::add);
        if (items.size() < request.orderItemIds().size()) {
            throw new NoSuchElementException("Could not find some items. Please check the orderItemIds.");
        }
        Double totalAmount = items.stream()
                .map(i -> i.getQuantity() * i.getProduct().getPrice())
                .reduce(0.0, Double::sum);
        order.setTotal_amount(totalAmount);
        order.setOrderItems(items);
        Order saved = orderRepository.save(order);
        List<OrderItem> assignmentItems = items.stream().peek(orderItem -> orderItem.setOrder(saved)).toList();
        orderItemRepository.saveAll(assignmentItems);
        return new OrderResponse(
                saved.getId(),
                saved.getTotal_amount(),
                assignmentItems.stream().map(orderItem -> new OrderItemResponse(
                        orderItem.getId(),
                        orderItem.getQuantity(),
                        orderItem.getProduct().getSku(),
                        orderItem.getOrder().getId()
                )).toList());
    }
}
