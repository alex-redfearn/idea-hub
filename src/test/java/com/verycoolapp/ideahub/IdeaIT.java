package com.verycoolapp.ideahub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verycoolapp.ideahub.model.request.CreateIdeaRequest;
import com.verycoolapp.ideahub.model.request.CreateUserRequest;
import com.verycoolapp.ideahub.model.response.CreateIdeaResponse;
import com.verycoolapp.ideahub.model.response.CreateUserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.verycoolapp.ideahub.UserIT.USER_ENDPOINT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class IdeaIT extends MySqlTestContainer {

    public static final String IDEA_ENDPOINT = "/idea/%s";
    private static final String LIGHT_BULB = "Light Bulb!";

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final RestRequest restRequest;

    public IdeaIT(@Autowired MockMvc mockMvc) {
        this.restRequest = new RestRequest(mockMvc, OBJECT_MAPPER);
    }

    @Test
    public void givenANewIdea_whenCreateRequest_thenReturnCreated() {
        // GIVEN A new user
        CreateUserRequest createUserRequest = new CreateUserRequest()
                .setEmail("john.fox@verycoolapp.com")
                .setFirstName("John")
                .setLastName("Fox");

        MockHttpServletRequestBuilder createUser = restRequest.build(USER_ENDPOINT, createUserRequest);
        CreateUserResponse userResponse = restRequest.perform(createUser, HttpStatus.CREATED, CreateUserResponse.class);
        assertTrue(userResponse.id() > 0);

        // AND A new idea
        CreateIdeaRequest createIdeaRequest = new CreateIdeaRequest()
                .setName(LIGHT_BULB)
                .setImagePath("https://www.cdn.vercoolapp.com/light-bulb");

        // WHEN A create request is sent
        MockHttpServletRequestBuilder createIdea = restRequest.build(String.format(IDEA_ENDPOINT, userResponse.id()), createIdeaRequest);

        // THEN A 201 response should be returned
        CreateIdeaResponse ideaResponse = restRequest.perform(createIdea, HttpStatus.CREATED, CreateIdeaResponse.class);

        // AND A new ID
        assertTrue(ideaResponse.getId() > 0);

        // AND The correct idea name
        assertEquals(LIGHT_BULB, ideaResponse.getName());
    }

    @Test
    public void givenNewIdeaWithMissingName_whenCreateRequest_thenReturnBadRequest() {
        // GIVEN A new idea with a missing name!
        CreateIdeaRequest createIdeaRequest = new CreateIdeaRequest()
                .setImagePath("https://www.cdn.vercoolapp.com/random-image");

        // WHEN A create request is sent
        MockHttpServletRequestBuilder createIdea = restRequest.build(String.format(IDEA_ENDPOINT, 1L), createIdeaRequest);

        // THEN A 400 response should be returned
        restRequest.perform(createIdea, HttpStatus.BAD_REQUEST);
    }

}
