package com.verycoolapp.ideahub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestRequest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public RestRequest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public MockHttpServletRequestBuilder buildPost(final String uri, final Object content) {
        try {
            return post(uri)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MockHttpServletRequestBuilder buildGet(final String uri) {
        try {
            return get(uri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MvcResult perform(final MockHttpServletRequestBuilder request, final HttpStatus expectedStatus) {
        try {
            return mockMvc.perform(request)
                    .andExpect(status().is(expectedStatus.value()))
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T perform(final MockHttpServletRequestBuilder request, final HttpStatus expectedStatus, TypeReference<T> typeReference) {
        try {
            MvcResult perform = perform(request, expectedStatus);
            return objectMapper.readValue(perform.getResponse().getContentAsString(), typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
