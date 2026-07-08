package com.apigatewayplatform.userservice.service;

import com.apigatewayplatform.userservice.dto.UserRequest;
import com.apigatewayplatform.userservice.dto.UserResponse;
import com.apigatewayplatform.userservice.entity.User;
import com.apigatewayplatform.userservice.exception.DuplicateUserException;
import com.apigatewayplatform.userservice.exception.UserNotFoundException;
import com.apigatewayplatform.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return convertToResponse(user);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateUserException(userRequest.getEmail());
        }
        User user = convertToEntity(userRequest);
        User createdUser = userRepository.save(user);
        return convertToResponse(createdUser);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userRequest.getFirstName() != null) {
            user.setFirstName(userRequest.getFirstName());
        }
        if (userRequest.getLastName() != null) {
            user.setLastName(userRequest.getLastName());
        }
        if (userRequest.getEmail() != null && !userRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new DuplicateUserException(userRequest.getEmail());
            }
            user.setEmail(userRequest.getEmail());
        }
        if (userRequest.getPhone() != null) {
            user.setPhone(userRequest.getPhone());
        }
        if (userRequest.getAddress() != null) {
            user.setAddress(userRequest.getAddress());
        }
        if (userRequest.getCity() != null) {
            user.setCity(userRequest.getCity());
        }
        if (userRequest.getState() != null) {
            user.setState(userRequest.getState());
        }
        if (userRequest.getZipCode() != null) {
            user.setZipCode(userRequest.getZipCode());
        }

        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }

    private User convertToEntity(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setAddress(userRequest.getAddress());
        user.setCity(userRequest.getCity());
        user.setState(userRequest.getState());
        user.setZipCode(userRequest.getZipCode());
        return user;
    }

    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAddress(user.getAddress());
        response.setCity(user.getCity());
        response.setState(user.getState());
        response.setZipCode(user.getZipCode());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }

}
