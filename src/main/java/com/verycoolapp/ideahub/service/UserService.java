package com.verycoolapp.ideahub.service;

import com.verycoolapp.ideahub.exception.UserExistsException;
import com.verycoolapp.ideahub.model.entity.User;
import com.verycoolapp.ideahub.model.request.CreateUserRequest;
import com.verycoolapp.ideahub.model.response.UserResponse;
import com.verycoolapp.ideahub.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse create(@Valid CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new UserExistsException();

        User newUser = new User()
                .setEmail(request.getEmail())
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName());

        com.verycoolapp.ideahub.model.entity.User savedUser = userRepository.save(newUser);

        return new UserResponse(savedUser.getId());
    }

}
