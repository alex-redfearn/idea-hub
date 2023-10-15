package com.verycoolapp.ideahub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verycoolapp.ideahub.model.request.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserIT {

    private static final String USER_ENDPOINT = "/user";

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final MockMvc mockMvc;

    public UserIT(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void givenNewUser_whenCreateRequest_thenReturnCreated() {
        // GIVEN A new user
        CreateUserRequest createUserRequest = new CreateUserRequest()
                .setEmail("alex.redfearn@verycoolapp.com")
                .setFirstName("Alex")
                .setLastName("Redfearn");

        // WHEN A create request is sent
        // THEN A 201 response should be returned
        restPost(USER_ENDPOINT, createUserRequest, HttpStatus.CREATED);
    }

    private void restPost(final String uri, final Object content, final HttpStatus expectedHttpStatus) {
        try {
            MockHttpServletRequestBuilder requestBuilder = org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(uri)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .content(content == null ? "" : OBJECT_MAPPER.writeValueAsString(content));

            mockMvc.perform(requestBuilder)
                    .andExpect(status().is(expectedHttpStatus.value()))
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
