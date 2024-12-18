package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Order;
import com.bymdev.artem.ecommercedemo.entity.OrderItem;
import com.bymdev.artem.ecommercedemo.repository.OrderItemRepository;
import com.bymdev.artem.ecommercedemo.repository.OrderRepository;
import com.bymdev.artem.ecommercedemo.request.OrderRequest;
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

    public Order getOrder(int id) {
        return orderRepository.findById(id).orElseThrow();
    }

    public List<Order> getOrders(int count, int page) {
        Iterable<Order> all = orderRepository.findAll(PageRequest.of(page, count));
        List<Order> orders = new ArrayList<>();
        all.forEach(orders::add);
        return orders;
    }

    public void createOrder(OrderRequest request) {
        List<OrderItem> items = new ArrayList<>();
        orderItemRepository.findAllById(request.orderItemIds()).forEach(items::add);
        if (items.size() < request.orderItemIds().size()) {
            throw new NoSuchElementException("Could not find some items. Please check the orderItemIds.");
        }
        Double totalAmount = items.stream()
                .map(i -> i.getQuantity() * i.getProduct().getPrice())
                .reduce(0.0, Double::sum);
        Order order = new Order(null, totalAmount, items);
        orderRepository.save(order);
    }

    public void updateOrder(int id, OrderRequest request) {

    }
}
