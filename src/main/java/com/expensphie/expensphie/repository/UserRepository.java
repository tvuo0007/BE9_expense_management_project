package com.expensphie.expensphie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expensphie.expensphie.model.UserEntity;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long>{
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByActivationToken(String activationToken);
}
