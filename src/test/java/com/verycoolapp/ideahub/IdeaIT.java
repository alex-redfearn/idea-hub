package com.verycoolapp.ideahub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.verycoolapp.ideahub.model.request.CreateIdeaRequest;
import com.verycoolapp.ideahub.model.request.CreateUserRequest;
import com.verycoolapp.ideahub.model.response.IdeaResponse;
import com.verycoolapp.ideahub.model.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static com.verycoolapp.ideahub.UserIT.USER_ENDPOINT;
import static com.verycoolapp.ideahub.UserIT.USER_TYPE_REFERENCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class IdeaIT extends MySqlTestContainer {

    public static final String IDEA_ENDPOINT = "/idea/%s";
    private static final String LIGHT_BULB = "Light Bulb!";
    private static final TypeReference<IdeaResponse> IDEA_RESPONSE_TYPE_REFERENCE = new TypeReference<>() {
    };
    private static final TypeReference<List<IdeaResponse>> IDEA_RESPONSE_LIST_TYPE_REFERENCE = new TypeReference<>() {
    };

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

        MockHttpServletRequestBuilder createUser = restRequest.buildPost(USER_ENDPOINT, createUserRequest);
        UserResponse user = restRequest.perform(createUser, HttpStatus.CREATED, USER_TYPE_REFERENCE);
        assertTrue(user.id() > 0);

        // AND A new idea
        CreateIdeaRequest createIdeaRequest = new CreateIdeaRequest()
                .setName(LIGHT_BULB)
                .setImagePath("https://www.cdn.vercoolapp.com/light-bulb");

        // WHEN A create request is sent
        MockHttpServletRequestBuilder createIdea = restRequest.buildPost(String.format(IDEA_ENDPOINT, user.id()), createIdeaRequest);

        // THEN A 201 response should be returned
        IdeaResponse ideaResponse = restRequest.perform(createIdea, HttpStatus.CREATED, IDEA_RESPONSE_TYPE_REFERENCE);

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
        MockHttpServletRequestBuilder createIdea = restRequest.buildPost(String.format(IDEA_ENDPOINT, 1L), createIdeaRequest);

        // THEN A 400 response should be returned
        restRequest.perform(createIdea, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenAnExistingIdea_whenGetIdeasForUserRequest_thenReturnIdeas() {
        // GIVEN An existing user
        CreateUserRequest createUserRequest = new CreateUserRequest()
                .setEmail("peter.piper@verycoolapp.com")
                .setFirstName("John")
                .setLastName("Fox");

        MockHttpServletRequestBuilder createUser = restRequest.buildPost(USER_ENDPOINT, createUserRequest);
        UserResponse user = restRequest.perform(createUser, HttpStatus.CREATED, USER_TYPE_REFERENCE);

        // AND An existing idea
        CreateIdeaRequest createIdeaRequest = new CreateIdeaRequest()
                .setName("Super Cool Idea!")
                .setImagePath("https://www.cdn.vercoolapp.com/cool");

        MockHttpServletRequestBuilder createCoolIdea = restRequest.buildPost(String.format(IDEA_ENDPOINT, user.id()), createIdeaRequest);
        restRequest.perform(createCoolIdea, HttpStatus.CREATED);

        // WHEN Get Ideas is called
        MockHttpServletRequestBuilder getIdea = restRequest.buildGet(String.format(IDEA_ENDPOINT, user.id()));

        // THEN A list of all the users ideas should be returned
        List<IdeaResponse> idea = restRequest.perform(getIdea, HttpStatus.OK, IDEA_RESPONSE_LIST_TYPE_REFERENCE);
        assertEquals(idea.size(), 1);
        assertEquals(idea.get(0).getName(), createIdeaRequest.getName());
    }

}
