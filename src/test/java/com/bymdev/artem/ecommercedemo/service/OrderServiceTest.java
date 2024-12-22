package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.*;
import com.bymdev.artem.ecommercedemo.repository.OrderItemRepository;
import com.bymdev.artem.ecommercedemo.repository.OrderRepository;
import com.bymdev.artem.ecommercedemo.repository.SearchRepository;
import com.bymdev.artem.ecommercedemo.request.OrderRequest;
import com.bymdev.artem.ecommercedemo.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SearchRepository searchRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private SearchService searchService;

    @Mock
    private Page<Order> orderPage;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(searchService, orderRepository, searchRepository, orderItemRepository);
    }

    @Test
    void testGetOrder() {
        // Arrange
        int orderId = 1;
        Order order = new Order(orderId, 100.0, new ArrayList<>(), new java.sql.Timestamp(System.currentTimeMillis()));
        List<OrderItem> orderItems = new ArrayList<>();
        order.setOrderItems(orderItems);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        OrderResponse orderResponse = orderService.getOrder(orderId);

        // Assert
        assertNotNull(orderResponse);
        assertEquals(orderId, orderResponse.id());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetOrders() {
        // Arrange
        int count = 5;
        int page = 0;
        Order order = new Order(1, 100.0, new ArrayList<>(), new java.sql.Timestamp(System.currentTimeMillis()));
        when(orderPage.stream()).thenReturn(Stream.of(order));
        when(orderRepository.findAll(PageRequest.of(page, count))).thenReturn(orderPage);

        // Act
        List<OrderResponse> orderResponses = orderService.getOrders(count, page);

        // Assert
        assertNotNull(orderResponses);
        assertEquals(1, orderResponses.size());
        verify(orderRepository, times(1)).findAll(PageRequest.of(page, count));
    }

    @Test
    void testCreateOrder() {
        // Arrange
        Product product = Mockito.mock(Product.class);
        OrderRequest orderRequest = new OrderRequest(List.of(1, 2)); // Example orderItemIds
        List<OrderItem> orderItems = List.of(
                new OrderItem(1, 1, product, null),
                new OrderItem(2, 2, product, null)
        );
        Order savedOrder = new Order(null, 30.0, orderItems, new java.sql.Timestamp(System.currentTimeMillis()));

        when(product.getPrice()).thenReturn(10.0);
        when(product.getCategory()).thenReturn(Mockito.mock(Category.class));
        when(orderItemRepository.findAllById(orderRequest.orderItemIds())).thenReturn(orderItems);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(searchService.getOrderDoc(any())).thenReturn(new OrderDoc("sku", "product", List.of(1)));

        // Act
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(orderResponse);
        assertEquals(30.0, orderResponse.total_amount());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateOrder() {
        // Arrange
        int orderId = 1;
        List<Integer> orderItemIds = List.of(1, 2);
        OrderRequest orderRequest = new OrderRequest(orderItemIds);
        ArgumentCaptor<List<OrderItem>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        Product product = Mockito.mock(Product.class);
        List<OrderItem> orderItems = List.of(
                new OrderItem(1, 1, product, null),
                new OrderItem(2, 2, product, null)
        );
        Order order = new Order(orderId, 30.0, orderItems, new java.sql.Timestamp(System.currentTimeMillis()));

        when(product.getPrice()).thenReturn(10.0);
        when(product.getCategory()).thenReturn(Mockito.mock(Category.class));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(searchService.getOrderDoc(any())).thenReturn(new OrderDoc("sku", "product", List.of(orderId)));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemRepository.findAllById(orderItemIds)).thenReturn(orderItems);

        // Act
        OrderResponse orderResponse = orderService.updateOrder(orderId, orderRequest);

        // Assert
        assertNotNull(orderResponse);
        assertEquals(30.0, orderResponse.total_amount());
        verify(searchRepository, times(1)).saveAll(any(Iterable.class));
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderItemRepository, times(1)).saveAll(argumentCaptor.capture());
        argumentCaptor.getValue().forEach(orderItem -> assertEquals(order.getId(), orderItem.getOrder().getId()));
    }

    @Test
    void testUpdateOrderWithException() {
        // Arrange
        int orderId = 1;
        List<Integer> orderItemIds = List.of(1, 2);
        OrderRequest orderRequest = new OrderRequest(orderItemIds);
        Product product = Mockito.mock(Product.class);
        List<OrderItem> orderItems = List.of(
                new OrderItem(1, 1, product, null),
                new OrderItem(2, 2, product, null)
        );
        Order order = new Order(orderId, 30.0, orderItems, new java.sql.Timestamp(System.currentTimeMillis()));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemRepository.findAllById(orderItemIds)).thenReturn(List.of());

        // Assert
        assertThrows(NoSuchElementException.class, () -> orderService.updateOrder(orderId, orderRequest));
    }

    @Test
    void testDeleteOrder() {
        // Arrange
        int orderId = 1;
        Order order = new Order(orderId, 200.0, new ArrayList<>(), new java.sql.Timestamp(System.currentTimeMillis()));

        when(orderItemRepository.findAllByOrder_Id(orderId)).thenReturn(new ArrayList<>());
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        orderService.delete(orderId);

        // Assert
        verify(orderRepository, times(1)).deleteById(orderId);
        verify(orderItemRepository, times(1)).findAllByOrder_Id(orderId);
    }

    @Test
    void testDeletedItemsFromOrder() {
        // Arrange
        int orderId = 1;
        Product product = Mockito.mock(Product.class);
        Order order = new Order(orderId, 200.0, new ArrayList<>(), new java.sql.Timestamp(System.currentTimeMillis()));
        List<OrderItem> orderItems = List.of(new OrderItem(1,1, product, order));

        when(orderItemRepository.findAllByOrder_Id(orderId)).thenReturn(orderItems);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(searchService.getOrderDoc(any())).thenReturn(new OrderDoc("sku", "product", List.of()));

        // Act
        orderService.delete(orderId);

        // Assert
        verify(orderRepository, times(1)).deleteById(orderId);
        verify(searchRepository, times(1)).deleteAll(any(Iterable.class));
        verify(orderItemRepository, times(1)).findAllByOrder_Id(orderId);
    }

    @Test
    void testSaveOrder() {
        // Arrange
        Product product = Mockito.mock(Product.class);
        List<OrderItem> orderItems = List.of(
                new OrderItem(1, 1, product, null),
                new OrderItem(2, 2, product, null)
        );
        OrderRequest orderRequest = new OrderRequest(List.of(1, 2));
        Order order = new Order(null, 250.0, orderItems, new java.sql.Timestamp(System.currentTimeMillis()));

        when(product.getCategory()).thenReturn(Mockito.mock(Category.class));
        when(orderItemRepository.findAllById(orderRequest.orderItemIds())).thenReturn(order.getOrderItems());
        when(searchService.getOrderDoc(any())).thenReturn(new OrderDoc("sku", "product", List.of(1)));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(orderResponse);
        assertEquals(250.0, orderResponse.total_amount());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
