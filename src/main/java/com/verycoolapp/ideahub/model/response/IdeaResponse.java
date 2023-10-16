package com.verycoolapp.ideahub.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class IdeaResponse {

    private Long id;
    private Long userId;
    private String name;
    private String imagePath;

    private int likes;
    private List<Comment> comments;

}
