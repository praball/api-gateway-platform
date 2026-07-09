package com.apigatewayplatform.orderservice.service;

import com.apigatewayplatform.orderservice.client.UserClient;
import com.apigatewayplatform.orderservice.dto.OrderRequest;
import com.apigatewayplatform.orderservice.dto.OrderResponse;
import com.apigatewayplatform.orderservice.entity.Order;
import com.apigatewayplatform.orderservice.exception.OrderNotFoundException;
import com.apigatewayplatform.orderservice.exception.UserNotFoundException;
import com.apigatewayplatform.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserClient userClient;

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return convertToResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = convertToEntity(orderRequest);
        if(!userClient.userExists(orderRequest.getUserId())) {
            throw new UserNotFoundException(orderRequest.getUserId());
        }
        Order createdOrder = orderRepository.save(order);
        return convertToResponse(createdOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (orderRequest.getUserId() != null) {
            if (!userClient.userExists(orderRequest.getUserId())) {
                throw new UserNotFoundException(orderRequest.getUserId());
            }
            order.setUserId(orderRequest.getUserId());
        }
        if (orderRequest.getStatus() != null) {
            order.setStatus(orderRequest.getStatus());
        }
        if (orderRequest.getTotalAmount() != null) {
            order.setTotalAmount(orderRequest.getTotalAmount());
        }
        if (orderRequest.getShippingAddress() != null) {
            order.setShippingAddress(orderRequest.getShippingAddress());
        }
        if (orderRequest.getBillingAddress() != null) {
            order.setBillingAddress(orderRequest.getBillingAddress());
        }
        if (orderRequest.getPaymentMethod() != null) {
            order.setPaymentMethod(orderRequest.getPaymentMethod());
        }

        Order updatedOrder = orderRepository.save(order);
        return convertToResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        orderRepository.delete(order);
    }

    private Order convertToEntity(OrderRequest orderRequest) {
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setStatus(orderRequest.getStatus());
        order.setTotalAmount(orderRequest.getTotalAmount());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setBillingAddress(orderRequest.getBillingAddress());
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setOrderNumber(generateOrderNumber());

        return order;
    }

    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setUserId(order.getUserId());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setShippingAddress(order.getShippingAddress());
        response.setBillingAddress(order.getBillingAddress());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
