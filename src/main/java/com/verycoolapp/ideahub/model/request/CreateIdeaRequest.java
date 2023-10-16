package com.verycoolapp.ideahub.model.request;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateIdeaRequest {

    @Hidden
    private Long userId;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "image path is required")
    private String imagePath;

}
