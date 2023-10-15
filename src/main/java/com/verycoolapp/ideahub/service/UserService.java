package com.verycoolapp.ideahub.service;

import com.verycoolapp.ideahub.model.request.CreateUserRequest;
import com.verycoolapp.ideahub.model.response.CreateUserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public CreateUserResponse create(CreateUserRequest request) {
        CreateUserResponse createUserResponse = new CreateUserResponse()
                .setId(1L);

        createUserResponse
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName());

        return createUserResponse;
    }

}
