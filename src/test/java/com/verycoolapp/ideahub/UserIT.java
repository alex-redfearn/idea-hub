package com.verycoolapp.ideahub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.verycoolapp.ideahub.model.request.CreateUserRequest;
import com.verycoolapp.ideahub.model.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserIT extends MySqlTestContainer {

    public static final String USER_ENDPOINT = "/user";
    private static final String FIRST_NAME = "Alex";
    private static final String LAST_NAME = "Redfearn";

    public static final TypeReference<UserResponse> USER_TYPE_REFERENCE = new TypeReference<>() {};
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final RestRequest restRequest;

    public UserIT(@Autowired MockMvc mockMvc) {
        this.restRequest = new RestRequest(mockMvc, OBJECT_MAPPER);
    }

    @Test
    public void givenNewUser_whenCreateRequest_thenReturnCreated() {
        // GIVEN A new user
        CreateUserRequest createUserRequest = new CreateUserRequest()
                .setEmail("alex.redfearn@verycoolapp.com")
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME);

        // WHEN A create request is sent
        MockHttpServletRequestBuilder request = restRequest.buildPost(USER_ENDPOINT, createUserRequest);

        // THEN A 201 response should be returned
        UserResponse perform = restRequest.perform(request, HttpStatus.CREATED, USER_TYPE_REFERENCE);

        // AND A new ID
        assertTrue(perform.id() > 0);
    }

    @Test
    public void givenNewUserWithMissingEmail_whenCreateRequest_thenReturnBadRequest() {
        //GIVEN A new user with a missing email
        CreateUserRequest createUserRequest = new CreateUserRequest()
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME);

        // WHEN A create request is sent
        MockHttpServletRequestBuilder request = restRequest.buildPost(USER_ENDPOINT, createUserRequest);

        // THEN A 400 response should be returned
        restRequest.perform(request, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenAnExistingUser_whenCreateRequest_thenReturnConflict() {
        // GIVEN An existing user
        CreateUserRequest createUserRequest = new CreateUserRequest()
                .setEmail("roger.badger@verycoolapp.com")
                .setFirstName("Roger")
                .setLastName("Badger");

        MockHttpServletRequestBuilder newUserRequest = restRequest.buildPost(USER_ENDPOINT, createUserRequest);
        restRequest.perform(newUserRequest, HttpStatus.CREATED);

        // WHEN A create request is sent for the existing user
        // THEN A 409 response should be returned
        restRequest.perform(newUserRequest, HttpStatus.CONFLICT);
    }

}
