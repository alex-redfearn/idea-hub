package com.verycoolapp.ideahub.service;

import com.verycoolapp.ideahub.exception.UserExistsException;
import com.verycoolapp.ideahub.model.entity.User;
import com.verycoolapp.ideahub.model.request.CreateUserRequest;
import com.verycoolapp.ideahub.model.response.CreateUserResponse;
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

    public CreateUserResponse create(@Valid CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new UserExistsException();

        User newUser = new User()
                .setEmail(request.getEmail())
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName());

        User savedUser = userRepository.save(newUser);

        return new CreateUserResponse(savedUser.getId());
    }

}
