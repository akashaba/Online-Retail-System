package edu.miu.cs.cs544.shopping.service.impl;

import edu.miu.cs.cs544.customer.domain.CreditCard;
import edu.miu.cs.cs544.customer.domain.Customer;
import edu.miu.cs.cs544.customer.repository.CustomerRepository;
import edu.miu.cs.cs544.product.domain.Product;
import edu.miu.cs.cs544.product.repository.ProductRepository;
import edu.miu.cs.cs544.shopping.domain.Order;
import edu.miu.cs.cs544.shopping.domain.OrderItem;
import edu.miu.cs.cs544.shopping.domain.OrderStatus;
import edu.miu.cs.cs544.shopping.repository.OrderRepository;
import edu.miu.cs.cs544.shopping.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder() {
        // Mock customer and product data
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John Doe");

        Product product = new Product();
        product.setId(1L);
        product.setName("Sample Product");
        product.setPrice(10.0);

        // Create an order with order items
        Order order = new Order();
        order.setCustomer(customer);

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItems.add(orderItem);

        order.setOrderItems(orderItems);

        // Mock repository behavior
        Mockito.when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        // Call the createOrder method
        Order createdOrder = orderService.createOrder(order);

        // Verify the behavior
        Assertions.assertNotNull(createdOrder);
        Assertions.assertEquals(order.getCustomer(), createdOrder.getCustomer());
        Assertions.assertEquals(order.getOrderItems().size(), createdOrder.getOrderItems().size());
        Assertions.assertEquals(order.getOrderItems().get(0).getProduct(), createdOrder.getOrderItems().get(0).getProduct());
        Assertions.assertEquals(order.getOrderStatus(), createdOrder.getOrderStatus());

        // Verify repository method calls
        Mockito.verify(customerRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(productRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
    }

    @Test
    void addItemToOrder() throws OrderNotFoundException {
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.NEW);

        Product product = new Product();
        product.setId(1L);
        product.setName("Sample Product");
        product.setPrice(10.0);

        // Create an order item
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        // Mock repository behavior
        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        // Call the addItemToOrder method
        Order updatedOrder = orderService.addItemToOrder(1L, orderItem);

        // Verify the behavior
        Assertions.assertNotNull(updatedOrder);
        Assertions.assertEquals(order, updatedOrder);
        Assertions.assertEquals(1, updatedOrder.getOrderItems().size());
        Assertions.assertEquals(orderItem, updatedOrder.getOrderItems().get(0));

        // Verify repository method calls
        Mockito.verify(orderRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(productRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
    }

    @Test
    void orderPayment() throws OrderNotFoundException {
        // Mock order and credit card data
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.NEW);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John Doe");

        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCardNumber("1234567890123456");

        List<CreditCard> creditCards = new ArrayList<>();
        creditCards.add(creditCard);

        customer.setCreditCard(creditCards);
        order.setCustomer(customer);

        // Mock repository behavior
        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        // Call the orderPayment method
        Order updatedOrder = orderService.orderPayment(1L, creditCard);

        // Verify the behavior
        Assertions.assertNotNull(updatedOrder);
        Assertions.assertEquals(order, updatedOrder);
        Assertions.assertEquals(creditCard, updatedOrder.getPaymentMethod());
        Assertions.assertEquals(OrderStatus.PROCESSED, updatedOrder.getOrderStatus());

        // Verify repository method calls
        Mockito.verify(orderRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
    }
}