package com.apigatewayplatform.orderservice.exception;

public class UserServiceUnavailableException extends RuntimeException {

    public UserServiceUnavailableException() {
        super("User Service is currently unavailable");
    }
}
