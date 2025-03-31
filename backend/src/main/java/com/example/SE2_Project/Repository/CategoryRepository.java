package com.example.SE2_Project.Repository;

import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByName(String name);
    List<CategoryEntity> findByUserUsername(String username);
    boolean existsByNameAndUser(String name, UserEntity user);



}
