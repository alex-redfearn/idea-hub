package com.verycoolapp.ideahub.model.response;

import com.verycoolapp.ideahub.model.request.CreateUserRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateUserResponse extends CreateUserRequest {

    private long id;

}
