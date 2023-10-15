package com.verycoolapp.ideahub.controller;

import com.verycoolapp.ideahub.model.request.CreateUserRequest;
import com.verycoolapp.ideahub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Creates a new user",
            description = "Creates a new user and returns their ID",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully created"),
                    @ApiResponse(responseCode = "409", description = "User already exists", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Oops something went wrong, please try again", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public void create(@RequestBody CreateUserRequest user) {
        log.debug("UserController.create, {}", user);
        userService.create(user);
    }

}
