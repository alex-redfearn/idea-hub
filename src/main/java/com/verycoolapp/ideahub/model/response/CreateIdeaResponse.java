package com.verycoolapp.ideahub.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateIdeaResponse {

    private Long id;
    private Long userId;
    private String name;
    private String imagePath;

}
