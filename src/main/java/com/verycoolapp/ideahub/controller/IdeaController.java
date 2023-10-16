package com.verycoolapp.ideahub.controller;

import com.verycoolapp.ideahub.model.request.CreateIdeaRequest;
import com.verycoolapp.ideahub.model.response.IdeaResponse;
import com.verycoolapp.ideahub.service.IdeaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class IdeaController {

    private final IdeaService ideaService;

    public IdeaController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    @PostMapping(path = "idea/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Creates a new idea!",
            description = "Creates an idea associated with a user. A valid user ID must be passed in the url." +
                    "A user can be created by calling the /user endpoint.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Idea successfully created"),
                    @ApiResponse(responseCode = "500", description = "Oops something went wrong, please try again", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public ResponseEntity<IdeaResponse> create(@PathVariable("userId") Long userId, @RequestBody CreateIdeaRequest idea) {
        idea.setUserId(userId);
        log.debug("IdeaController.create, {}", idea);
        return new ResponseEntity<>(ideaService.create(idea), HttpStatus.CREATED);
    }

    @GetMapping(path = "ideas", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Gets all the ideas!",
            description = "Gets a list of ideas.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Idea successfully created"),
                    @ApiResponse(responseCode = "500", description = "Oops something went wrong, please try again", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public ResponseEntity<List<IdeaResponse>> get() {
        log.debug("IdeaController.get");
        return new ResponseEntity<>(ideaService.getIdeas(), HttpStatus.OK);
    }

}
