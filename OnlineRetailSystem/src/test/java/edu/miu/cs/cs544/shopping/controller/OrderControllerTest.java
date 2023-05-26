package edu.miu.cs.cs544.shopping.controller;

import edu.miu.cs.cs544.customer.domain.CreditCard;
import edu.miu.cs.cs544.shopping.domain.Order;
import edu.miu.cs.cs544.shopping.domain.OrderItem;
import edu.miu.cs.cs544.shopping.domain.OrderStatus;
import edu.miu.cs.cs544.shopping.service.OrderService;
import edu.miu.cs.cs544.shopping.service.impl.OrderNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;
    @Test
    void createOrder() {
        // Mock order data
        Order order = new Order();
        order.setId(1L);
        // Set other necessary properties

        // Mock orderService behavior
        Mockito.when(orderService.createOrder(any(Order.class))).thenReturn(order);

        // Call the createOrder method
        ResponseEntity<Order> responseEntity = orderController.createOrder(order);

        // Verify the behavior
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(order, responseEntity.getBody());

        // Verify orderService method calls
        Mockito.verify(orderService, Mockito.times(1)).createOrder(any(Order.class));
    }

    @Test
    void addItemToOrder() throws OrderNotFoundException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        // Set other necessary properties

        // Mock orderService behavior
        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        // Set other necessary properties

        Mockito.when(orderService.addItemToOrder(anyLong(), any(OrderItem.class))).thenReturn(updatedOrder);

        // Call the addItemToOrder method
        ResponseEntity<Order> responseEntity = orderController.addItemToOrder(1L, orderItem);

        // Verify the behavior
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(updatedOrder, responseEntity.getBody());

        // Verify orderService method calls
        Mockito.verify(orderService, Mockito.times(1)).addItemToOrder(anyLong(), any(OrderItem.class));
    }

    @Test
    void orderPayment() throws OrderNotFoundException {
        // Mock credit card data
        CreditCard creditCard = new CreditCard();
        creditCard.setId(1L);
        // Set other necessary properties

        // Mock orderService behavior
        Order paidOrder = new Order();
        paidOrder.setId(1L);
        // Set other necessary properties

        Mockito.when(orderService.orderPayment(anyLong(), any(CreditCard.class))).thenReturn(paidOrder);

        // Call the orderPayment method
        ResponseEntity<Order> responseEntity = orderController.orderPayment(1L, creditCard);

        // Verify the behavior
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(paidOrder, responseEntity.getBody());

        // Verify orderService method calls
        Mockito.verify(orderService, Mockito.times(1)).orderPayment(anyLong(), any(CreditCard.class));
    }

    @Test
    void getOrders() throws OrderNotFoundException {
        long orderId = 1L;

        // Call the processShipping method
        ResponseEntity<Void> responseEntity = orderController.processShipping(orderId);

        // Verify the behavior
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify orderService method calls
        Mockito.verify(orderService, Mockito.times(1)).setStatus(orderId, OrderStatus.PROCESSED);
    }

    @Test
    void getOrderDetails() {
        long orderId = 1L;

        // Mock orderService behavior
        Order order = new Order();
        order.setId(orderId);
        // Set other necessary properties

        Mockito.when(orderService.getOrderDetail(anyLong())).thenReturn(order);

        // Call the getOrderDetails method
        ResponseEntity<?> responseEntity = orderController.getOrderDetails(orderId);

        // Verify the behavior
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(order, responseEntity.getBody());

        // Verify orderService method calls
        Mockito.verify(orderService, Mockito.times(1)).getOrderDetail(orderId);
    }
}