package com.example.SE2_Project.Repository;

import com.example.SE2_Project.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsernameAndStatusIsFalse(String username);
    Optional<UserEntity> findByUsername(String username);
}
