package com.verycoolapp.ideahub.repository;

import com.verycoolapp.ideahub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface UserRepository extends JpaRepository<User, BigInteger> {

    boolean existsByEmail(String email);

}
