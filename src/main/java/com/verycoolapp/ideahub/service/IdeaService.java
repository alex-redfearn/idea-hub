package com.verycoolapp.ideahub.service;

import com.verycoolapp.ideahub.model.entity.Idea;
import com.verycoolapp.ideahub.model.entity.User;
import com.verycoolapp.ideahub.model.request.CreateIdeaRequest;
import com.verycoolapp.ideahub.model.response.Comment;
import com.verycoolapp.ideahub.model.response.IdeaResponse;
import com.verycoolapp.ideahub.repository.IdeaRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@Service
public class IdeaService {

    private final IdeaRepository ideaRepository;

    public IdeaService(IdeaRepository ideaRepository) {
        this.ideaRepository = ideaRepository;
    }

    public IdeaResponse create(@Valid CreateIdeaRequest idea) {
        log.debug("Saving idea!");
        Idea save = ideaRepository.save(new com.verycoolapp.ideahub.model.entity.Idea()
                .setUser(new User().setId(idea.getUserId()))
                .setName(idea.getName())
                .setImagePath(idea.getImagePath())
        );

        return new IdeaResponse()
                .setId(save.getId())
                .setName(save.getName())
                .setImagePath(save.getImagePath());
    }

    public List<IdeaResponse> getIdeas(long userId) {
        log.debug("Finding ideas for user, {}", userId);

        List<Idea> ideas = ideaRepository.findIdeasByUser(new User().setId(userId));

        // Collected ideas now mapping them to the response class.
        return ideas.stream()
                .map(idea -> new IdeaResponse()
                        .setName(idea.getName())
                        .setImagePath(idea.getImagePath())
                        // Could have separate calls to get all the comments for an idea.
                        .setComments(idea.getComments()
                                .stream()
                                .map(comment -> new Comment(comment.getText()))
                                .collect(Collectors.toList())
                        )
                        // Likewise... with likes. Probably more performant to just get the count initially.
                        // Once a user actually expands the likes then fetch the list of users who have liked.
                        .setLikes(idea.getLikes().size()))
                .toList();
    }

}
