package com.verycoolapp.ideahub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verycoolapp.ideahub.model.request.CreateUserRequest;
import com.verycoolapp.ideahub.model.response.CreateUserResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserIT extends MySqlTestContainer {

    private static final String USER_ENDPOINT = "/user";
    private static final String FIRST_NAME = "Alex";
    private static final String LAST_NAME = "Redfearn";
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final MockMvc mockMvc;

    public UserIT(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public static void startDatabase() {
        mysqlContainer.start();
    }

    @Test
    public void givenNewUser_whenCreateRequest_thenReturnCreated() {
        // GIVEN A new user
        CreateUserRequest createUserRequest = new CreateUserRequest()
                .setEmail("alex.redfearn@verycoolapp.com")
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME);

        // WHEN A create request is sent
        MockHttpServletRequestBuilder request = build(USER_ENDPOINT, createUserRequest);

        // THEN A 201 response should be returned
        CreateUserResponse perform = perform(request, HttpStatus.CREATED, CreateUserResponse.class);

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
        MockHttpServletRequestBuilder request = build(USER_ENDPOINT, createUserRequest);

        // THEN A 404 response should be returned
        perform(request, HttpStatus.BAD_REQUEST, List.class);
    }

    @Test
    public void givenAnExistingUser_whenCreateRequest_thenReturnConflict() {
        // GIVEN An existing user
        CreateUserRequest createUserRequest = new CreateUserRequest()
                .setEmail("roger.badger@verycoolapp.com")
                .setFirstName("Roger")
                .setLastName("Badger");

        MockHttpServletRequestBuilder newUserRequest = build(USER_ENDPOINT, createUserRequest);
        perform(newUserRequest, HttpStatus.CREATED, CreateUserResponse.class);

        // WHEN A create request is sent for the existing user
        // THEN A 404 response should be returned
        perform(newUserRequest, HttpStatus.CONFLICT);
    }

    private MockHttpServletRequestBuilder build(final String uri, final Object content) {
        try {
            return org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(uri)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .content(OBJECT_MAPPER.writeValueAsString(content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MvcResult perform(final MockHttpServletRequestBuilder request, final HttpStatus expectedStatus) {
        try {
            return mockMvc.perform(request)
                    .andExpect(status().is(expectedStatus.value()))
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T perform(final MockHttpServletRequestBuilder request, final HttpStatus expectedStatus, Class<T> responseType) {
        try {
            MvcResult perform = perform(request, expectedStatus);
            return OBJECT_MAPPER.readValue(perform.getResponse().getContentAsString(), responseType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
