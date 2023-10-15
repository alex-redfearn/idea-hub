package com.verycoolapp.ideahub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verycoolapp.ideahub.model.request.CreateUserRequest;
import com.verycoolapp.ideahub.model.response.CreateUserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
                .setFirstName("Alex")
                .setLastName("Redfearn");

        // WHEN A create request is sent
        // THEN A 201 response should be returned
        CreateUserResponse createUserResponse = restPost(USER_ENDPOINT, createUserRequest, HttpStatus.CREATED, CreateUserResponse.class);

        // AND A CreateUserResponse body
        assertNotNull(createUserResponse);
        // AND A new ID
        assertTrue(createUserResponse.getId() > 0);
    }

    private <T> T restPost(final String uri, final Object content, final HttpStatus expectedHttpStatus, final Class<T> responseType) {
        try {
            MockHttpServletRequestBuilder requestBuilder = org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(uri)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .content(content == null ? "" : OBJECT_MAPPER.writeValueAsString(content));

            MvcResult mvcResult = mockMvc.perform(requestBuilder)
                    .andExpect(status().is(expectedHttpStatus.value()))
                    .andReturn();
            return responseType == null ? null : OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), responseType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
