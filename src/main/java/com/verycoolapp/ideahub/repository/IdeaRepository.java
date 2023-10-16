package com.verycoolapp.ideahub.repository;

import com.verycoolapp.ideahub.model.entity.Idea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IdeaRepository extends JpaRepository<Idea, BigInteger> {
}
