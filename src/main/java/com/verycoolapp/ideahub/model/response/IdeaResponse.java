package com.verycoolapp.ideahub.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class IdeaResponse {

    private Long id;
    private String name;
    private UserResponse user;
    private String imagePath;

    private int likes;
    private int comments;

}
