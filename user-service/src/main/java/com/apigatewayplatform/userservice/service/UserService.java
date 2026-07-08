package com.apigatewayplatform.userservice.service;

import com.apigatewayplatform.userservice.dto.UserRequest;
import com.apigatewayplatform.userservice.dto.UserResponse;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse createUser(UserRequest userRequest);

    UserResponse updateUser(Long id, UserRequest userRequest);

    void deleteUser(Long id);

}
