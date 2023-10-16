package com.verycoolapp.ideahub.service;

import com.verycoolapp.ideahub.model.entity.Idea;
import com.verycoolapp.ideahub.model.entity.User;
import com.verycoolapp.ideahub.model.request.CreateIdeaRequest;
import com.verycoolapp.ideahub.model.response.CreateIdeaResponse;
import com.verycoolapp.ideahub.repository.IdeaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class IdeaService {

    private final IdeaRepository ideaRepository;

    public IdeaService(IdeaRepository ideaRepository) {
        this.ideaRepository = ideaRepository;
    }

    public CreateIdeaResponse create(@Valid CreateIdeaRequest idea) {
        Idea save = ideaRepository.save(new Idea()
                .setUser(new User().setId(idea.getUserId()))
                .setName(idea.getName())
                .setImagePath(idea.getImagePath())
        );

        return new CreateIdeaResponse()
                .setId(save.getId())
                .setName(save.getName())
                .setImagePath(save.getImagePath());
    }

}
