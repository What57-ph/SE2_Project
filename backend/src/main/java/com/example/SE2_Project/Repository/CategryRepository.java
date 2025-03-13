package com.example.SE2_Project.Repository;

import com.example.SE2_Project.Entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategryRepository extends JpaRepository<CategoryEntity, Long> {


}
