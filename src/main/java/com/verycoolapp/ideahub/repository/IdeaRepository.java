package com.verycoolapp.ideahub.repository;

import com.verycoolapp.ideahub.model.entity.Idea;
import com.verycoolapp.ideahub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface IdeaRepository extends JpaRepository<Idea, BigInteger> {

    List<Idea> findIdeasByUser(@Param("user") User user);

}
