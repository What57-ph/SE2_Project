package com.example.SE2_Project.Repository;

import com.example.SE2_Project.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsernameAndStatusIsFalse(String username);
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findByRole(String role);
    List<UserEntity> findByStatus(boolean status);
    List<UserEntity> findByRoleAndStatus(String role, boolean status);
    long count(); // Đếm tất cả users
}
