package com.apigatewayplatform.orderservice.service;

import com.apigatewayplatform.orderservice.dto.OrderRequest;
import com.apigatewayplatform.orderservice.dto.OrderResponse;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long id);

    OrderResponse createOrder(OrderRequest orderRequest);

    OrderResponse updateOrder(Long id, OrderRequest orderRequest);

    void deleteOrder(Long id);

}
