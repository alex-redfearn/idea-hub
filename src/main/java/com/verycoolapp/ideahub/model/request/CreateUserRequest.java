package com.verycoolapp.ideahub.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateUserRequest {

    private String firstName;
    private String lastName;

}
