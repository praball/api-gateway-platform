package com.apigatewayplatform.orderservice.service;

import com.apigatewayplatform.orderservice.entity.Order;
import com.apigatewayplatform.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder(Order order) {
        if (orderRepository.existsByOrderNumber(order.getOrderNumber())) {
            throw new IllegalArgumentException("Order with order number " + order.getOrderNumber() + " already exists");
        }
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));

        if (orderDetails.getOrderNumber() != null && !orderDetails.getOrderNumber().equals(order.getOrderNumber())) {
            if (orderRepository.existsByOrderNumber(orderDetails.getOrderNumber())) {
                throw new IllegalArgumentException("Order with order number " + orderDetails.getOrderNumber() + " already exists");
            }
            order.setOrderNumber(orderDetails.getOrderNumber());
        }
        if (orderDetails.getUserId() != null) {
            order.setUserId(orderDetails.getUserId());
        }
        if (orderDetails.getStatus() != null) {
            order.setStatus(orderDetails.getStatus());
        }
        if (orderDetails.getTotalAmount() != null) {
            order.setTotalAmount(orderDetails.getTotalAmount());
        }
        if (orderDetails.getShippingAddress() != null) {
            order.setShippingAddress(orderDetails.getShippingAddress());
        }
        if (orderDetails.getBillingAddress() != null) {
            order.setBillingAddress(orderDetails.getBillingAddress());
        }
        if (orderDetails.getPaymentMethod() != null) {
            order.setPaymentMethod(orderDetails.getPaymentMethod());
        }

        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
        orderRepository.delete(order);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
}
