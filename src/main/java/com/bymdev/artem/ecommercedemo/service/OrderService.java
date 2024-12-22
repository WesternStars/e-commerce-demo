package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Order;
import com.bymdev.artem.ecommercedemo.entity.OrderDoc;
import com.bymdev.artem.ecommercedemo.entity.OrderItem;
import com.bymdev.artem.ecommercedemo.repository.OrderItemRepository;
import com.bymdev.artem.ecommercedemo.repository.OrderRepository;
import com.bymdev.artem.ecommercedemo.repository.SearchRepository;
import com.bymdev.artem.ecommercedemo.request.OrderRequest;
import com.bymdev.artem.ecommercedemo.response.OrderResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class OrderService {

    private final SearchService searchService;
    private final OrderRepository orderRepository;
    private final SearchRepository searchRepository;
    private final OrderItemRepository orderItemRepository;

    public static OrderResponse getOrderResponse(Order order, List<OrderItem> items) {
        return new OrderResponse(
                order.getId(),
                order.getTotal_amount(),
                order.getCreatedAt(),
                items.stream().map(OrderItemService::getOrderItemResponse).toList()
        );
    }

    private static List<OrderItem> getRefreshOrderItem(List<OrderItem> orderItems) {
        return getRefreshOrderItem(orderItems, null);
    }

    private static List<OrderItem> getRefreshOrderItem(List<OrderItem> orderItems, Order saved) {
        return orderItems.stream().peek(item -> item.setOrder(saved)).toList();
    }

    public OrderResponse getOrder(int id) {
        Order order = orderRepository.findById(id).orElseThrow();
        return getOrderResponse(order, order.getOrderItems());
    }

    public List<OrderResponse> getOrders(int count, int page) {
        return orderRepository.findAll(PageRequest.of(page, count))
                .stream()
                .map(order -> getOrderResponse(order, order.getOrderItems()))
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
        Optional<Order> optionalOrder = orderRepository.findById(id);
        optionalOrder.ifPresent(order -> doDelete(id));
    }

    private OrderResponse saveOrder(OrderRequest request) {
        return saveOrder(request, null);
    }

    private OrderResponse saveOrder(OrderRequest request, Integer id) {
        Order order = mapToOrder(id, request);
        List<OrderItem> orderItems = order.getOrderItems();

        List<OrderItem> deletedItems = getRefreshOrderItem(deletedItemsFromOrder(id, orderItems));
        List<OrderItem> attachedItems = getRefreshOrderItem(orderItems, order);
        List<OrderItem> allItems = concat(attachedItems, deletedItems);
        orderItemRepository.saveAll(allItems);
        updateElasticIndex(allItems);

        return getOrderResponse(order, attachedItems);
    }

    private List<OrderItem> concat(List<OrderItem> list1, List<OrderItem> list2) {
        return Stream.concat(list1.stream(), list2.stream()).toList();
    }

    private List<OrderItem> deletedItemsFromOrder(Integer id, List<OrderItem> newItems) {
        List<OrderItem> oldItems = orderItemRepository.findAllByOrder_Id(id);
        if (oldItems.size() > newItems.size()) {
            return oldItems.stream().filter(i -> !newItems.contains(i)).toList();
        }
        return List.of();
    }

    private void updateElasticIndex(List<OrderItem> items) {
        Map<Boolean, List<OrderDoc>> orderDocs = items.stream()
                .map(item -> searchService.getOrderDoc(item.getProduct()))
                .collect(Collectors.partitioningBy(o -> o.getOrderIdList().isEmpty()));
        searchRepository.saveAll(orderDocs.get(false));
        searchRepository.deleteAll(orderDocs.get(true));
    }

    private void doDelete(int id) {
        orderRepository.deleteById(id);
        updateElasticIndex(getRefreshOrderItem(deletedItemsFromOrder(id, List.of())));
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

    private Double calculateTotalAmount(List<OrderItem> items) {
        return items.stream()
                .map(i -> i.getQuantity() * i.getProduct().getPrice())
                .reduce(0.0, Double::sum);
    }
}
