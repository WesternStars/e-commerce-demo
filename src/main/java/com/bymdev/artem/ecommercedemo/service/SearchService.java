package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Order;
import com.bymdev.artem.ecommercedemo.entity.OrderDoc;
import com.bymdev.artem.ecommercedemo.entity.OrderItem;
import com.bymdev.artem.ecommercedemo.entity.Product;
import com.bymdev.artem.ecommercedemo.repository.OrderItemRepository;
import com.bymdev.artem.ecommercedemo.repository.OrderRepository;
import com.bymdev.artem.ecommercedemo.repository.SearchRepository;
import com.bymdev.artem.ecommercedemo.response.OrderResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.bymdev.artem.ecommercedemo.service.OrderService.getOrderResponse;
import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
public class SearchService {

    public static final String ORDER_ID = "id";

    private final OrderItemRepository orderItemRepository;
    private final SearchRepository searchRepository;
    private final OrderRepository orderRepository;

    private static OrderDoc getOrderDoc(Map.Entry<Product, List<Order>> entry) {
        Product product = entry.getKey();
        List<Order> orders = entry.getValue();
        return new OrderDoc(
                product.getSku(),
                product.getName(),
                orders.stream().map(Order::getId).toList()
        );
    }

    public List<OrderResponse> search(String namePart, int page, int count) {
        Set<Integer> orders = searchRepository.findAllByPartProductName(namePart, PageRequest.of(page, count))
                .stream()
                .map(OrderDoc::getOrderIdList)
                .flatMap(Collection::stream)
                .collect(toSet());
        return getOrderResponses(orderRepository.findAllById(orders));
    }

    public void reindex() {
        List<OrderDoc> orderDocs = orderItemRepository.findAllByOrderIdIsNotNull()
                .stream()
                .collect(groupingBy(OrderItem::getProduct, mapping(OrderItem::getOrder, toList())))
                .entrySet()
                .stream()
                .map(SearchService::getOrderDoc)
                .toList();
        searchRepository.saveAll(orderDocs);
    }

    public OrderDoc getOrderDoc(Product product) {
        List<Integer> orderIdListWithProduct = orderRepository.findAllOrderWithProduct(product.getSku())
                .stream()
                .map(row -> (Integer) row.get(ORDER_ID))
                .toList();
        return new OrderDoc(product.getSku(), product.getName(), orderIdListWithProduct);
    }

    private List<OrderResponse> getOrderResponses(Iterable<Order> orders) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        orders.forEach(order -> orderResponses.add(getOrderResponse(order, order.getOrderItems())));
        return orderResponses;
    }
}
