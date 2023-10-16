package com.verycoolapp.ideahub.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class UserResponse {

    private long id;
    private String email;
    private String firstName;
    private String lastName;

}
