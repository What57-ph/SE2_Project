package com.example.SE2_Project.Repository;

import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByName(String name);
    List<CategoryEntity> findByUsersUsername(String username);
    boolean existsByNameAndUsers(String name, UserEntity user);
    Optional<CategoryEntity> findByNameIgnoreCase(String name);
    List<CategoryEntity> findAllByIdBetween(Long startId, Long endId);

}
